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
package mage.cards.e;

import mage.MageInt;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.effects.Effect;
import mage.abilities.effects.common.DrawCardTargetEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SuperType;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.events.DamagedPlayerEvent;
import mage.game.events.GameEvent;
import mage.game.events.GameEvent.EventType;
import mage.target.targetpointer.FixedTarget;

import java.util.UUID;

/**
 * @author mluds
 */
public class EdricSpymasterOfTrest extends CardImpl {

    public EdricSpymasterOfTrest(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{1}{G}{U}");
        addSuperType(SuperType.LEGENDARY);
        this.subtype.add("Elf");
        this.subtype.add("Rogue");

        this.power = new MageInt(2);
        this.toughness = new MageInt(2);

        // Whenever a creature deals combat damage to one of your opponents, its controller may draw a card.
        this.addAbility(new EdricSpymasterOfTrestTriggeredAbility());
    }

    public EdricSpymasterOfTrest(final EdricSpymasterOfTrest card) {
        super(card);
    }

    @Override
    public EdricSpymasterOfTrest copy() {
        return new EdricSpymasterOfTrest(this);
    }
}

class EdricSpymasterOfTrestTriggeredAbility extends TriggeredAbilityImpl {

    public EdricSpymasterOfTrestTriggeredAbility() {
        super(Zone.BATTLEFIELD, new DrawCardTargetEffect(1, true), false);
    }

    public EdricSpymasterOfTrestTriggeredAbility(final EdricSpymasterOfTrestTriggeredAbility ability) {
        super(ability);
    }

    @Override
    public EdricSpymasterOfTrestTriggeredAbility copy() {
        return new EdricSpymasterOfTrestTriggeredAbility(this);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == EventType.DAMAGED_PLAYER;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        if (((DamagedPlayerEvent) event).isCombatDamage() && 
                game.getOpponents(this.controllerId).contains(((DamagedPlayerEvent) event).getPlayerId())) {
            for (Effect effect : this.getEffects()) {
                effect.setTargetPointer(new FixedTarget(game.getPermanent(event.getSourceId()).getControllerId()));
            }
            return true;
        }
        return false;
    }

    @Override
    public String getRule() {
        return "Whenever a creature deals combat damage to one of your opponents, its controller may draw a card";
    }
}
