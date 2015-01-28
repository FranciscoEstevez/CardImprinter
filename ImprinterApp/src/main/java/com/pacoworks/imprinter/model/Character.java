
package com.pacoworks.imprinter.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paco on 25/01/2015. See LICENSE.md
 */
public class Character extends Card {
    public String name;

    public String archetype;

    public int initiative;

    public List<Skill> skills = new ArrayList<>();

    public Character() {
    }

    public int getHP() {
        int count = 0;
        for (Skill skill : skills) {
            count += skill.quantity;
        }
        return count;
    }

    @Override
    public String alternativeUnsanitizedFileName() {
        return name;
    }
}
