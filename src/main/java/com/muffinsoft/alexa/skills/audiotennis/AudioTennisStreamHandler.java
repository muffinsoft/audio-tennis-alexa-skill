package com.muffinsoft.alexa.skills.audiotennis;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.muffinsoft.alexa.sdk.handlers.ResponseExceptionHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisActionIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisCancelIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisFallbackIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisHelpIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisLaunchRequestHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisResetIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.handlers.TennisStopIntentHandler;

public class AudioTennisStreamHandler extends SkillStreamHandler {

    public AudioTennisStreamHandler() {
        super(getSkill());
    }

    private static Skill getSkill() {

        String amazonSkillId = System.getProperty("amazon-skill-id");

        return Skills.standard()
                .addRequestHandlers(
                        new TennisActionIntentHandler(IoC.provideIntentFactory()),
                        new TennisCancelIntentHandler(IoC.provideConfigurationContainer()),
                        new TennisFallbackIntentHandler(IoC.provideConfigurationContainer()),
                        new TennisHelpIntentHandler(IoC.provideConfigurationContainer()),
                        new TennisLaunchRequestHandler(IoC.provideConfigurationContainer()),
                        new TennisStopIntentHandler(IoC.provideConfigurationContainer()),
                        new TennisResetIntentHandler(IoC.provideConfigurationContainer())
                )
                .addExceptionHandler(new ResponseExceptionHandler())
                .withSkillId(amazonSkillId)
                .withTableName("audio-tennis")
                .build();
    }
}
