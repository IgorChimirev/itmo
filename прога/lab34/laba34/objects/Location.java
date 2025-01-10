package objects;

import java.util.HashMap;
import java.util.Map;
import constans.PlaceConstans;

public class Location {
    private Map<PlaceConstans, Integer> locations;

    public Location() {
        locations = new HashMap<>();
        initializeLocations();
    }

    private void initializeLocations() {
        locations.put(PlaceConstans.CAFE, 0);
        locations.put(PlaceConstans.PALACE, 0);
        locations.put(PlaceConstans.CLUB, 0);
        locations.put(PlaceConstans.PARK, 0);
        locations.put(PlaceConstans.HOSPITAL, 0);
        locations.put(PlaceConstans.STABLE, 0);
        locations.put(PlaceConstans.PARK, 0);
    }

    public void incrementPeopleCount(PlaceConstans location) {
        if (locations.containsKey(location)) {
            locations.put(location, locations.get(location) + 1);
            displayPeopleCount(location);
        }
    }

    public void displayPeopleCount(PlaceConstans location) {
        if (locations.containsKey(location)) {
            System.out.println("В локации " + location + " теперь " + locations.get(location) + " человек.");
        }
    }
}
