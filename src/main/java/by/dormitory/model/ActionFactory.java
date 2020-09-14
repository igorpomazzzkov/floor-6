package by.dormitory.model;

import by.dormitory.config.TableConfiguration;
import by.dormitory.entity.Duty;
import by.dormitory.entity.User;
import by.dormitory.service.DutyService;
import by.dormitory.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.MessageAttachment;
import com.vk.api.sdk.objects.photos.Image;
import com.vk.api.sdk.objects.photos.Photo;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ActionFactory {

    private static final String regexAll = "/[а-я]+\\s+\\d+-[1,2]";
    private static final String regexMessage = "/[а-я]+\\s+";
    private static final String regexGraph = "/[а-я]+\\s+\\d+";
    private static final String dutyResponse = "Дежурство на %s %d смена - %s";
    private static final String errorOfDate = "Неверная дата/время";
    private static final String infoNameFormat = "&#8252; Статистика %s &#8252;  \n\nОжидающие дежурства: \n%s \n\nВыполненые дежурства: \n%s";
    private static final String botInfoFormat = "Бот дежурств 6 этажа. " +
            "\nАвтор: Igor Pomazkov " +
            "\nВерсия: 0.0.4" +
            "\n\nСписок функций:" +
            "\n/инфо &#8212; Информация" +
            "\n/запись <день>-<смена> &#8212; Запись на дежурство следующего месяца" +
            "\n/отмена <день>-<смена> &#8212; Отмена дежурства следующего месяца " +
            "\n/я &#8212; Информация о дежурства пользователя " +
            "\n/график <месяц> &#8212; Вывод графика дежурств";
    private final UserService userService;
    private final DutyService dutyService;
    private final TableConfiguration tableConfiguration;

    public ActionFactory(UserService userService, DutyService dutyService, TableConfiguration tableConfiguration) {
        this.userService = userService;
        this.dutyService = dutyService;
        this.tableConfiguration = tableConfiguration;
    }

    public Message getMessageByAction(Message message, User user) throws MalformedURLException {
        String messageAction = message.getText();
        String returnMessage = null;
        String messageData = null;
        Message msgResponse = new Message();
        if (message.getText().matches(regexAll) || message.getText().matches(regexGraph)) {
            messageAction = regexMessageAction(message.getText());
            messageData = regexMessageData(message.getText());
        }
        switch (Objects.requireNonNull(messageAction).toLowerCase()) {
            case "/запись": {
                returnMessage = this.add(messageData, user);
                break;
            }
            case "/отмена": {
                returnMessage = this.cancel(messageData, user);
                break;
            }
            case "/инфо": {
                returnMessage = this.botInfo();
                break;
            }
            case "/я": {
                returnMessage = this.info(user);
                break;
            }
            case "/график": {
                if (messageData == null) {
                    messageData = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
                }
                returnMessage = this.graph(user, messageData);
                MessageAttachment messageAttachment = new MessageAttachment();
                Image image = new Image();
                image.setUrl(new File("file" + File.separator + "image.jpg").toURI().toURL());
                ArrayList<Image> images = new ArrayList<>();
                images.add(image);
                Photo photo = new Photo();
                photo.setImages(images);
                messageAttachment.setPhoto(photo);
                List<MessageAttachment> messageAttachments = new ArrayList<>();
                messageAttachments.add(messageAttachment);
                msgResponse.setAttachments(messageAttachments);
                break;
            }
        }
        msgResponse.setText(returnMessage);
        return msgResponse;
    }

    private String botInfo() {
        return botInfoFormat;
    }

    private String regexMessageData(String message) {
        return message.split(regexMessage)[1];
    }

    private String regexMessageAction(String message) {
        Pattern pattern = Pattern.compile(regexMessage);
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            return message.substring(matcher.start(), matcher.end()).trim();
        }
        return null;
    }

    private String formattingDate(Date date) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, new Locale("ru"));
        return dateFormat.format(date);
    }

    private String add(String data, User user) {
        if (data == null) {
            return "Введите день и смену \n Пример: /запись 1-1";
        }
        Integer day = this.getDay(data);
        Integer halt = this.getHalf(data);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        Calendar currentCalendar = new GregorianCalendar(year, month, day);
        if (!dayIsValid(currentCalendar)) {
            return errorOfDate;
        }
        Duty duty = new Duty(currentCalendar.getTime(), halt, user);
        List<Duty> allDuties = dutyService.findAll();
        List<Duty> dutiesFromDB = allDuties.stream().filter(d -> {
            return d.equals(duty);
        }).collect(Collectors.toList());
        if (!dutiesFromDB.isEmpty()) {
            return String.format(dutyResponse, formattingDate(currentCalendar.getTime()), halt, "занято");
        }
        allDuties.add(duty);
        if (user.getDuties() == null) {
            user.setDuties(new ArrayList<>());
        }
        user.getDuties().add(duty);
        userService.save(user);
        this.tableConfiguration.insertToFile(user, day, halt);
        return String.format(dutyResponse, formattingDate(currentCalendar.getTime()), halt, "принято");
    }

    private boolean dayIsValid(Calendar calendar) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.MONTH, date.get(Calendar.MONTH) + 1);
        date.set(Calendar.YEAR, date.get(Calendar.YEAR));
        date.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
        if (date.get(Calendar.MONTH) != calendar.get(Calendar.MONTH)) {
            return false;
        }
//        if (date.get(Calendar.DAY_OF_MONTH) < todayCalendar.get(Calendar.DAY_OF_MONTH)) {
//            return false;
//        }
//        if (date.get(Calendar.DAY_OF_MONTH) == todayCalendar.get(Calendar.DAY_OF_MONTH)) {
//            int half = duty.getHalf();
//            int hourDuty = LocalDateTime.now().getHour();
//            if ((half == 1 && hourDuty > 8)) {
//                return false;
//            }
//            if ((half == 2 && hourDuty > 16)) {
//                return false;
//            }
//        }
        return true;
    }

    private Integer getDay(String data) {
        return Integer.parseInt(data.split("-")[0]);
    }

    private Integer getHalf(String data) {
        return Integer.parseInt(data.split("-")[1]);
    }

    private String info(User user) {
        Calendar date = Calendar.getInstance();
        String dutyUpcoming = user.getDuties().stream()
                .filter(u -> {
                    Calendar todayCalendar = Calendar.getInstance();
                    todayCalendar.setTime(u.getDate());
                    return date.get(Calendar.MONTH) < todayCalendar.get(Calendar.MONTH) ||
                            (date.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH) &&
                            date.get(Calendar.DAY_OF_MONTH) <= todayCalendar.get(Calendar.DAY_OF_MONTH));
                })
                .sorted(Comparator.comparing(Duty::getDate))
                .map(u -> formattingDate(u.getDate()) + " " + u.getHalf() + " смена").collect(Collectors.joining("\n"));

        String dutyComplete = user.getDuties().stream()
                .filter(u -> {
                    Calendar todayCalendar = Calendar.getInstance();
                    todayCalendar.setTime(u.getDate());
                    return date.get(Calendar.MONTH) > todayCalendar.get(Calendar.MONTH) ||
                            (date.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH) &&
                            date.get(Calendar.DAY_OF_MONTH) >= todayCalendar.get(Calendar.DAY_OF_MONTH));
                })
                .sorted(Comparator.comparing(Duty::getDate))
                .map(u -> formattingDate(u.getDate()) + " " + u.getHalf() + " смена").collect(Collectors.joining("\n"));
        return String.format(infoNameFormat, user.getFirstName() + " " + user.getLastName(), dutyUpcoming, dutyComplete);
    }

    private String cancel(String data, User user) {
        if (data == null) {
            return "Введите день и смену \n Пример: /запись 1-1";
        }
        Integer day = this.getDay(data);
        Integer halt = this.getHalf(data);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        Calendar currentCalendar = new GregorianCalendar(year, month, day);
        if (!dayIsValid(currentCalendar)) {
            return errorOfDate;
        }
        Duty duty = new Duty(currentCalendar.getTime(), halt, user);
        List<Duty> dutyToCancel = user.getDuties().stream().filter(d -> {
            return d.equals(duty);
        }).collect(Collectors.toList());
        if (dutyToCancel.isEmpty()) {
            return "Вы не можете отменить это дежурство";
        }
        user.getDuties().remove(duty);
        dutyService.delete(dutyToCancel.get(0));
        this.tableConfiguration.insertToFile(null, day, halt);
        return "Вы отменили свое дежурство " + formattingDate(currentCalendar.getTime());
    }

    private String graph(User user, String name) {
        File file = new File("files" + File.separator + name + ".xls");
        Workbook workbook = new Workbook();
        workbook.loadFromFile(file.getAbsolutePath());
        Worksheet worksheet = workbook.getWorksheets().get(name);
        worksheet.saveToImage("files" + File.separator + "image.jpg");
        workbook.save();
        workbook.dispose();
        return "График дежурств";
    }
}
