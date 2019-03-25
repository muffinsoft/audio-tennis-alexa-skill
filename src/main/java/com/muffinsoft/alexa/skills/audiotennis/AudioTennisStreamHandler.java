package com.muffinsoft.alexa.skills.audiotennis;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.muffinsoft.alexa.sdk.handlers.ResponseExceptionHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisActionIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisActionOnlyIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisCancelIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisFallbackIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisHelpIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisLaunchRequestHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisMenuIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisMissionNavigationIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisNavigationHomeIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisNoIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisOneActionOnlyIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisResetIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisSelectActivityIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisSelectActivityOnlyIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisStartOverIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisStopIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisSwitchActivityIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisYesIntentHandler;

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
                        new TennisMenuIntentHandler(IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer()),
                        new TennisStartOverIntentHandler(IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer()),
                        new TennisCancelIntentHandler(IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer()),
                        new TennisFallbackIntentHandler(IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer()),
                        new TennisHelpIntentHandler(IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer()),
                        new TennisLaunchRequestHandler(IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer()),
                        new TennisStopIntentHandler(IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer()),
                        new TennisResetIntentHandler(IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer())
                )
                .addExceptionHandler(new ResponseExceptionHandler())
                .withSkillId(amazonSkillId)
                .withTableName("audio-tennis")
                .build();
    }
}
