package org.jboss.set.pull.processor;

/**
 * Determine type of PR/phase that processor and its actors will go through.
 *
 * @author baranowb
 *
 */
public enum ProcessorPhase {
    // TODO: most likely this can be equal to PR state, but with some hacks?
    // TODO: follow up on above or possibly use int ID likish thing, so processor can define it by itself?
    OPEN, EVENTS_CLOSED_UPGRADE;
}
