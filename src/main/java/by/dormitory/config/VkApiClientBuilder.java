package by.dormitory.config;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.queries.messages.MessagesGetConversationsByIdQuery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
public class VkApiClientBuilder {

    private final CallbackApiHandler callbackApiHandler;
    private final VkApiClient vkApiClient;
    private final GroupActor groupActor;
    private final UserActor userActor;

    @Value("${GROUP_ID}")
    public Integer groupId;

    public VkApiClientBuilder(CallbackApiHandler callbackApiHandler, VkApiClient vkApiClient, GroupActor groupActor, UserActor userActor) {
        this.callbackApiHandler = callbackApiHandler;
        this.vkApiClient = vkApiClient;
        this.groupActor = groupActor;
        this.userActor = userActor;
    }

    @PostConstruct
    public void startHandler(){
        try {
            this.vkApiClient.groups()
                    .setLongPollSettings(this.groupActor, groupId)
                    .messageDeny(true)
                    .messageAllow(true)
                    .apiVersion("5.101")
                    .enabled(true)
                    .messageNew(true)
                    .execute();
            callbackApiHandler.run();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }
}
