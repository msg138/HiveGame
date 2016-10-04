package com.hivegame.game.ability;

import com.retro.engine.component.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 9/25/2016.
 */
public class AbilityRosterComponent extends Component {

    private List<Ability> m_abilities;

    public AbilityRosterComponent(){
        m_abilities = new ArrayList<>();
    }

    public AbilityRosterComponent(Ability[] nability){
        this();
        for(int i=0;i<nability.length; i++){
            m_abilities.add(nability[i]);
        }
    }

    public Ability getAbility(int index){
        if(index < m_abilities.size())
            return m_abilities.get(index);
        return null;
    }

    public int getAmountOfAbilities(){
        return m_abilities.size();
    }

}
