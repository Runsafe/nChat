package no.runsafe.nchat.emotes;

import no.runsafe.framework.api.IOutput;
import no.runsafe.framework.api.event.player.IPlayerCommandPreprocessEvent;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerCommandPreprocessEvent;

public class EmoteHandler implements IPlayerCommandPreprocessEvent
{
	public EmoteHandler(IOutput output)
	{
		this.output = output;
	}

	@Override
	public void OnBeforePlayerCommand(RunsafePlayerCommandPreprocessEvent event)
	{
		output.fine("Command message: " + event.getMessage());
	}

	private IOutput output;
	private String[][] emotes = {
			{"bite", "%s bites their arm.", "%s runs over and bites %s."},
			{"kiss", "%s wants to be kissed.", "%s blows a kiss to %s."},
			{"slap", "%s slaps themself across the face.", "%s slaps %s in the face."},
			{"clap", "%s claps their hands.", "%s claps for %s."},
			{"cry", "%s cries.", "%s cries on %s's shoulder."},
			{"dropkick", "%s wants to be drop-kicked.", "%s drop-kicks %s to China."},
			{"wink", "%s winks.", "%s winks at %s."},
			{"wave", "%s waves .", "%s waves to %s."},
			{"look", "%s looks at themself.", "%s looks at %s."},
			{"sleep", "%s falls asleep.", "%s falls asleep on %s."},
			{"cheer", "%s cheers.", "%s cheers for %s."},
			{"smirk", "%s smirks.", "%s smirks at %s."},
			{"smile", "%s smiles.", "%s smiles at %s."},
			{"frown", "%s frowns.", "%s frowns at %s."},
			{"run", "%s runs away.", "%s runs away from %s."},
			{"dance", "%s dances.", "%s dances with %s."},
			{"yawn", "%s yawns.", "%s yawns at %s."},
			{"creep", "%s creeps around.", "%s creeps on %s."},
			{"tongue", "%s sticks out tongue.", "%s sticks tongue out at %s."},
			{"hug", "%s hugs themself.", "%s hugs %s."},
			{"bow", "%s takes a bow.", "%s bows down to %s."},
			{"drool", "%s drools.", "%s drools over %s."},
			{"blush", "%s blushes.", "%s blushes at %s."},
			{"punch", "%s punches their face.", "%s punches %s in the face."},
			{"stumble", "%s stumbles.", "%s stumbles over %s."},
			{"touch", "%s touches themself.", "%s touches %s."},
			{"poke", "%s pokes their eye.", "%s pokes %s."},
			{"die", "%s died.", "%s dies on %s."},
			{"skip", "%s skips around.", "%s skips around %s."},
			{"hide", "%s hides.", "%s hides from %s."},
			{"sing", "%s sings.", "%s sings to %s."},
			{"panic", "%s runs around in a panic.", "%s panics with %s."},
			{"whistle", "%s whistles.", "%s whistles at %s."},
			{"growl", "%s growls.", "%s growls at %s."},
			{"wait", "%s waits.", "%s waits for %s."},
			{"roar", "%s roars.", "%s roars at %s."},
			{"sigh", "%s sighs.", "%s sighs at %s."},
			{"giggle", "%s giggles.", "%s giggles at %s."},
			{"point", "%s points.", "%s points to %s."},
			{"eats", "%s eats their food.", "%s eats %s."},
			{"throw", "%s throws their room across the lunch.", "%s throws %s."},
			{"facepalm", "%s face-palms.", "%s face-palms because of %s."},
			{"shake", "%s shakes head.", "%s shakes head at %s."},
			{"glare", "%s glares.", "%s glares at %s."},
			{"cringe", "%s cringes.", "%s cringes at %s."},
			{"gasp", "%s gasps.", "%s gasps at %s."},
			{"highfive", "%s high-fives themself.", "%s jumps and high-fives %s."},
			{"lay", "%s lays down.", "%s lays down on %s."},
			{"jump", "%s jumps up and down.", "%s jumps on top of %s."},
			{"shrug", "%s shrugs.", "%s shrugs their shoulders at %s."},
			{"quit", "%s throws arms up and quits.", "%s quits with %s."},
			{"batman", "%s transforms into Batman.", "%s turns %s into Batman."},
			{"meow", "%s meows.", "%s meows at %s."},
			{"flip", "%s flips the table.", "%s flips %s over."},
	};
}
