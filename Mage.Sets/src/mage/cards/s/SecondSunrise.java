/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.s;

import mage.abilities.Ability;
import mage.abilities.effects.OneShotEffect;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.WatcherScope;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.ZoneChangeEvent;
import mage.watchers.Watcher;

import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author LevelX2
 */
public class SecondSunrise extends CardImpl {

    public SecondSunrise(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.INSTANT},"{1}{W}{W}");


        // Each player returns to the battlefield all artifact, creature, enchantment, and land cards in his or her graveyard that were put there from the battlefield this turn.
        this.getSpellAbility().addEffect(new SecondSunriseEffect());
        this.getSpellAbility().addWatcher(new SecondSunriseWatcher());
    }

    public SecondSunrise(final SecondSunrise card) {
        super(card);
    }

    @Override
    public SecondSunrise copy() {
        return new SecondSunrise(this);
    }
}

class SecondSunriseEffect extends OneShotEffect {

    SecondSunriseEffect() {
        super(Outcome.PutCardInPlay);
        staticText = "Each player returns to the battlefield all artifact, creature, enchantment, and land cards in his or her graveyard that were put there from the battlefield this turn";
    }

    SecondSunriseEffect(final SecondSunriseEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        SecondSunriseWatcher watcher = (SecondSunriseWatcher) game.getState().getWatchers().get(SecondSunriseWatcher.class.getSimpleName());
        if (watcher != null) {
            for (UUID id : watcher.cards) {
                Card c = game.getCard(id);
                if (c != null && game.getState().getZone(id) == Zone.GRAVEYARD) {
                    if (c.isArtifact() || c.isCreature() ||
                        c.isEnchantment() || c.isLand())
                    c.moveToZone(Zone.BATTLEFIELD, source.getSourceId(), game, false);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public SecondSunriseEffect copy() {
        return new SecondSunriseEffect(this);
    }
}

class SecondSunriseWatcher extends Watcher {
    ArrayList<UUID> cards = new ArrayList<>();

    public SecondSunriseWatcher() {
        super(SecondSunriseWatcher.class.getSimpleName(), WatcherScope.GAME);
    }

    public SecondSunriseWatcher(final SecondSunriseWatcher watcher) {
        super(watcher);
        this.cards.addAll(watcher.cards);
    }

    @Override
    public void watch(GameEvent event, Game game) {
        if (event.getType() == GameEvent.EventType.ZONE_CHANGE && ((ZoneChangeEvent)event).isDiesEvent()) {
            cards.add(event.getTargetId());
        }
    }

    @Override
    public SecondSunriseWatcher copy() {
        return new SecondSunriseWatcher(this);
    }

    @Override
    public void reset() {
        super.reset();
        cards.clear();
    }
}
