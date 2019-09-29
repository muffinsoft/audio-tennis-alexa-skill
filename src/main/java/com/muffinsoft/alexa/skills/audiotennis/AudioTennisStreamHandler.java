package com.muffinsoft.alexa.skills.audiotennis;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.muffinsoft.alexa.sdk.util.BaseResponseAssembler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.*;

public class AudioTennisStreamHandler extends SkillStreamHandler {

    public AudioTennisStreamHandler() {
        super(getSkill());
    }

    private static Skill getSkill() {

        String amazonSkillId = System.getProperty("amazon-skill-id");

        return Skills.standard()
                .addRequestHandlers(
                        new TennisActionIntentHandler(IoC.provideIntentFactory()),
                        new TennisActionOnlyIntentHandler(IoC.provideIntentFactory()),
                        new TennisMissionNavigationIntentHandler(IoC.provideIntentFactory()),
                        new TennisSelectActivityIntentHandler(IoC.provideIntentFactory()),
                        new TennisSelectActivityOnlyIntentHandler(IoC.provideIntentFactory()),
                        new TennisYesIntentHandler(IoC.provideIntentFactory()),
                        new TennisNoIntentHandler(IoC.provideIntentFactory()),
                        new TennisNavigationHomeIntentHandler(IoC.provideIntentFactory()),
                        new TennisSwitchActivityIntentHandler(IoC.provideIntentFactory()),
                        new TennisOneActionOnlyIntentHandler(IoC.provideIntentFactory()),
                        new TennisRefundIntentHandler(IoC.provideDialogtranslator(), IoC.providePhraseDependencyContainer()),
                        new TennisBuyIntentHandler(IoC.provideDialogtranslator(), IoC.providePhraseDependencyContainer()),
                        new TennisPurchaseHistoryHandler(IoC.provideDialogtranslator(), IoC.providePhraseDependencyContainer()),
                        new TennisWhatCanIBuyHandler(IoC.provideDialogtranslator(), IoC.providePhraseDependencyContainer()),
                        new TennisConnectionsResponseHandler(IoC.provideDialogtranslator(), IoC.providePhraseDependencyContainer(), new BaseResponseAssembler()),
                        new TennisBamWhamActionOnlyIntentHandler(IoC.provideIntentFactory()),
                        new TennisOneBamWhamActionOnlyIntentHandler(IoC.provideIntentFactory()),
//                        new TennisMenuIntentHandler(IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer()),
                        new TennisStartOverIntentHandler(IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer()),
                        new TennisCancelIntentHandler(IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer()),
                        new TennisFallbackIntentHandler(IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer()),
                        new TennisHelpIntentHandler(IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer()),
                        new TennisLaunchRequestHandler(IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer()),
//                        new TennisResetIntentHandler(IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer())
                        new TennisStopIntentHandler(IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer()),
                        new TennisRefundConnectionsResponseHandler()
                )
                //.addExceptionHandler(new ResponseExceptionHandler())
                .withSkillId(amazonSkillId)
                .withTableName("audio-tennis")
                .build();
    }
}
