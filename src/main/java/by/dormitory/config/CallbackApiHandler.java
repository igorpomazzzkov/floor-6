package by.dormitory.config;

import by.dormitory.entity.User;
import by.dormitory.model.ActionFactory;
import by.dormitory.service.DutyService;
import by.dormitory.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vk.api.sdk.callback.longpoll.CallbackApiLongPoll;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.callback.MessageAllow;
import com.vk.api.sdk.objects.callback.MessageDeny;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoUpload;
import com.vk.api.sdk.objects.photos.responses.MessageUploadResponse;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class CallbackApiHandler extends CallbackApiLongPoll {

    private final UserService userService;
    private final DutyService dutyService;
    private final VkApiClient vkApiClient;
    private final GroupActor groupActor;
    private final TableConfiguration tableConfiguration;

    @Value("${ACCESS_TOKEN}")
    public String accessToken;

    @Value("${GROUP_ID}")
    public Integer groupId;

    public CallbackApiHandler(UserService userService, DutyService dutyService, VkApiClient vkApiClient, GroupActor groupActor, TableConfiguration tableConfiguration) {
        super(vkApiClient, groupActor);
        this.userService = userService;
        this.dutyService = dutyService;
        this.vkApiClient = vkApiClient;
        this.groupActor = groupActor;
        this.tableConfiguration = tableConfiguration;
    }

    @Override
    public void messageAllow(Integer groupId, MessageAllow message) {
        System.out.println(message);
    }

    @Override
    public void messageDeny(Integer groupId, MessageDeny message) {
        System.out.println(message);
    }

    @Override
    public void messageNew(Integer groupId, Message message) {
        try {
            if(message.getText().equals("женева2020")){
                ObjectMapper mapper = new ObjectMapper();
                String json = " [{" +
                        "\"id\": 152139237," +
                        "\"firstName\": \"Владислав\"," +
                        "\"lastName\": \"Козелецкий\"," +
                        "\"isClosed\": true," +
                        "\"canAccessClosed\": false," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 156564246," +
                        "\"firstName\": \"Максим\"," +
                        "\"lastName\": \"Пытель\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 236005005," +
                        "\"firstName\": \"Света\"," +
                        "\"lastName\": \"Копать\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 133598326," +
                        "\"firstName\": \"Дарья\"," +
                        "\"lastName\": \"Выдрук\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 134985227," +
                        "\"firstName\": \"Юлия\"," +
                        "\"lastName\": \"Костяная\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 240890165," +
                        "\"firstName\": \"Ярослав\"," +
                        "\"lastName\": \"Михайлов\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 191912214," +
                        "\"firstName\": \"Кирилл\"," +
                        "\"lastName\": \"Калейчик\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 180033800," +
                        "\"firstName\": \"Игорь\"," +
                        "\"lastName\": \"Лукьянов\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 64007876," +
                        "\"firstName\": \"Евгений\"," +
                        "\"lastName\": \"Казаченко\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 135420603," +
                        "\"firstName\": \"Никита\"," +
                        "\"lastName\": \"Телюков\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 177615451," +
                        "\"firstName\": \"Alice\"," +
                        "\"lastName\": \"Dubrovskaya\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 134985227," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 201175982," +
                        "\"firstName\": \"Даниил\"," +
                        "\"lastName\": \"Суровый\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 184116233," +
                        "\"firstName\": \"Николай\"," +
                        "\"lastName\": \"Еросенков\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 146429629," +
                        "\"firstName\": \"Даша\"," +
                        "\"lastName\": \"Кузькина\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 238805322," +
                        "\"firstName\": \"Влад\"," +
                        "\"lastName\": \"Минеев\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 181450095," +
                        "\"firstName\": \"Никита\"," +
                        "\"lastName\": \"Киселёв\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 252830686," +
                        "\"firstName\": \"Сергей\"," +
                        "\"lastName\": \"Пятницкий\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 175059681," +
                        "\"firstName\": \"Максим\"," +
                        "\"lastName\": \"Качаловский\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 151335673," +
                        "\"firstName\": \"Виктория\"," +
                        "\"lastName\": \"Черникова\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 152625147," +
                        "\"firstName\": \"Владислав\"," +
                        "\"lastName\": \"Жидко\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 207556505," +
                        "\"firstName\": \"Саша\"," +
                        "\"lastName\": \"Бенчук\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 433743842," +
                        "\"firstName\": \"Максим\"," +
                        "\"lastName\": \"Мальцев\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 248383290," +
                        "\"firstName\": \"Никита\"," +
                        "\"lastName\": \"Базылев\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 219063282," +
                        "\"firstName\": \"Ангелина\"," +
                        "\"lastName\": \"Шестак\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 236005005," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 226218185," +
                        "\"firstName\": \"Илья\"," +
                        "\"lastName\": \"Ковалевич\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 180033800," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 268626597," +
                        "\"firstName\": \"Александр\"," +
                        "\"lastName\": \"Плашко\"," +
                        "\"isClosed\": true," +
                        "\"canAccessClosed\": false," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 216652911," +
                        "\"firstName\": \"Евгений\"," +
                        "\"lastName\": \"Дубина\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 142492421," +
                        "\"firstName\": \"Глеб\"," +
                        "\"lastName\": \"Климов\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 200104313," +
                        "\"firstName\": \"Кирилл\"," +
                        "\"lastName\": \"Сёмин\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 243596098," +
                        "\"firstName\": \"Мария\"," +
                        "\"lastName\": \"Овчинникова\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 189554709," +
                        "\"firstName\": \"Глебка\"," +
                        "\"lastName\": \"Буйчик\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 152499952," +
                        "\"firstName\": \"Владислав\"," +
                        "\"lastName\": \"Новик\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 185941674," +
                        "\"firstName\": \"Игорь\"," +
                        "\"lastName\": \"Коршунович\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 102049453," +
                        "\"firstName\": \"Виталик\"," +
                        "\"lastName\": \"Тищенко\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 178974281," +
                        "\"firstName\": \"Ваня\"," +
                        "\"lastName\": \"Романович\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 226295836," +
                        "\"firstName\": \"Андрей\"," +
                        "\"lastName\": \"Алешко\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 162530198," +
                        "\"firstName\": \"Макс\"," +
                        "\"lastName\": \"Медведев\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 155936128," +
                        "\"firstName\": \"Паша\"," +
                        "\"lastName\": \"Малашко\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 162530198," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 152827284," +
                        "\"firstName\": \"Максим\"," +
                        "\"lastName\": \"Коваль\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 224051064," +
                        "\"firstName\": \"Максим\"," +
                        "\"lastName\": \"Аврач\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 226218185," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 214587427," +
                        "\"firstName\": \"Андрей\"," +
                        "\"lastName\": \"Жариков\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 208231195," +
                        "\"firstName\": \"Павел\"," +
                        "\"lastName\": \"Новиков\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 190717524," +
                        "\"firstName\": \"Алексей\"," +
                        "\"lastName\": \"Соболев\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 455673834," +
                        "\"firstName\": \"Павел\"," +
                        "\"lastName\": \"Терешко\"," +
                        "\"isClosed\": true," +
                        "\"canAccessClosed\": false," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 156788929," +
                        "\"firstName\": \"Дмитрий\"," +
                        "\"lastName\": \"Герасимчик\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 160334013," +
                        "\"firstName\": \"Ирина\"," +
                        "\"lastName\": \"Григорьева\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 134985227," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 257048541," +
                        "\"firstName\": \"Дмитрий\"," +
                        "\"lastName\": \"Ляпкин\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 155936128," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 273521089," +
                        "\"firstName\": \"Павел\"," +
                        "\"lastName\": \"Хиженок\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 256053351," +
                        "\"firstName\": \"Екатерина\"," +
                        "\"lastName\": \"Железнова\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 134985227," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 198853006," +
                        "\"firstName\": \"Виктор\"," +
                        "\"lastName\": \"Лиховецкий\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 185941674," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 390243070," +
                        "\"firstName\": \"Igor\"," +
                        "\"lastName\": \"Pomazkov\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 198853006," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 250414932," +
                        "\"firstName\": \"Даниил\"," +
                        "\"lastName\": \"Нащинец\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 238805322," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 279775876," +
                        "\"firstName\": \"Николай\"," +
                        "\"lastName\": \"Адамов\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 390243070," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 162982179," +
                        "\"firstName\": \"Кирилл\"," +
                        "\"lastName\": \"Писарев\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 433743842," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 144141992," +
                        "\"firstName\": \"Алексей\"," +
                        "\"lastName\": \"Беленко\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 173377138," +
                        "\"firstName\": \"Влад\"," +
                        "\"lastName\": \"Марченко\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 152139237," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 133614543," +
                        "\"firstName\": \"Вадим\"," +
                        "\"lastName\": \"Гребнев\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 390243070," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 469501101," +
                        "\"firstName\": \"Тёна\"," +
                        "\"lastName\": \"Исаченко\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 177615451," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 135648335," +
                        "\"firstName\": \"Ярослав\"," +
                        "\"lastName\": \"Кириковский\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 257048541," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 181436080," +
                        "\"firstName\": \"Полина\"," +
                        "\"lastName\": \"Горанина\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 135648335," +
                        "\"type\": \"profile\"" +
                        "}, {" +
                        "\"id\": 153825793," +
                        "\"firstName\": \"Илья\"," +
                        "\"lastName\": \"Абрамовских\"," +
                        "\"isClosed\": false," +
                        "\"canAccessClosed\": true," +
                        "\"invitedBy\": 162982179," +
                        "\"type\": \"profile\"" +
                        "}]";

                Type listType = new TypeToken<ArrayList<User>>(){}.getType();
                List<User> users = new Gson().fromJson(json, listType);

                users.stream().forEach(u -> this.userService.save(u));
            }
            if (message.getText().startsWith("/")) {
                Message responseMessage = this.getActionByMessage(message);
                if (responseMessage != null) {
                    this.vkApiClient.messages().send(this.groupActor)
                            .peerId(message.getPeerId())
                            .randomId(message.getRandomId())
                            .message(responseMessage.getText())
                            .attachment("files/image.jpg")
                            .execute();
                }
                if (responseMessage.getAttachments() != null) {
                    PhotoUpload photoUpload = this.vkApiClient.photos().getMessagesUploadServer(groupActor).execute();
                    MessageUploadResponse response
                            = this.vkApiClient.upload()
                            .photoMessage(photoUpload.getUploadUrl().toString(), new File("files" + File.separator + "image.jpg"))
                            .execute();
                    List<Photo> photoList = this.vkApiClient.photos()
                            .saveMessagesPhoto(groupActor, response.getPhoto())
                            .server(response.getServer())
                            .hash(response.getHash())
                            .execute();

                    Photo photo = photoList.get(0);
                    String attachId = "photo" + photo.getOwnerId() + "_" + photo.getId();

                    this.vkApiClient.messages()
                            .send(groupActor)
                            .peerId(message.getPeerId())
                            .randomId(message.getRandomId())
                            .attachment(attachId)
                            .execute();
                }
            }
        } catch (ApiException | ClientException | IOException e) {
            e.printStackTrace();
        }
    }

    private Message getActionByMessage(Message message) throws MalformedURLException {
        User user = null;
        try {
            UserXtrCounters userVK = this.vkApiClient.users()
                    .get(new UserActor(groupId, accessToken))
                    .userIds(message.getFromId().toString())
                    .fields(Fields.DOMAIN).execute().get(0);
            user = userService.getUserById(userVK.getId());
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        ActionFactory actionFactory = new ActionFactory(userService, dutyService, tableConfiguration);
        return actionFactory.getMessageByAction(message, user);
    }

}