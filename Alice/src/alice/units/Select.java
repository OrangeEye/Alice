package alice.units;

import java.util.*;

import alice.AGame;
import alice.units.AUnit;
import bwapi.Unit;

/**
 * Diese Klasse ermöglicht es einfach Einheiten auszuwählen zB. einen Marine der am nähesten zu einer bestimmten Position steht.
 * Vom Prinzip her ist die Klasse eine Liste von Einheiten
 */
public class Select<T> {
	
	private List<T> data;
	
	// Constructor is private, use our(), enemy() or neutral() methods
    protected Select(Collection<T> unitsData) {
        data = new ArrayList<T>();
        data.addAll(unitsData);
    }
    
    private static List<AUnit> ourUnits() {
        List<AUnit> data = new ArrayList<AUnit>();

//        System.out.println("AGame.getPlayerUs().getUnits() = " + AGame.getPlayerUs().getUnits().size());
        for (Unit u : AGame.getPlayerUs().getUnits()) {
//            System.out.println(u);
//            System.out.println("******** " + AUnit.createFrom(u));
            data.add(AUnit.createFrom(u));
//            System.out.println(AUnit.createFrom(u));
        }

//        System.out.println("## Our size: " + data.size());
        return data;
    }
	
	/**
     * Selects our unfinished units.
     */
    public static Select<AUnit> ourNotFinished() {
        //Units units = new AUnits();
        List<AUnit> data = new ArrayList<AUnit>();

        for (AUnit unit : ourUnits()) {

            if (!unit.isCompleted()) {
                data.add(unit);
            }
        }

        return new Select<AUnit>(data);
    }

}
