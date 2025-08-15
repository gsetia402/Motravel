package org.moto.motravel.config;

import org.moto.motravel.model.*;
import org.moto.motravel.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private AdventureTypeRepository adventureTypeRepository;

    @Autowired
    private HiddenGemRepository hiddenGemRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize data only if tables are empty
        if (stateRepository.count() == 0) {
            initializeStates();
        }
        
        if (adventureTypeRepository.count() == 0) {
            initializeAdventureTypes();
        }
        
        if (hiddenGemRepository.count() == 0) {
            initializeHiddenGems();
        }
    }

    private void initializeStates() {
        // Create sample Indian states
        String[] stateNames = {
            "Maharashtra", "Himachal Pradesh", "Uttarakhand", "Rajasthan", "Kerala",
            "Karnataka", "Tamil Nadu", "Goa", "Gujarat", "Madhya Pradesh",
            "Jammu and Kashmir", "Ladakh", "Sikkim", "Meghalaya", "Assam"
        };

        for (String stateName : stateNames) {
            State state = new State(stateName);
            stateRepository.save(state);
        }
        
        System.out.println("Initialized " + stateNames.length + " states");
    }

    private void initializeAdventureTypes() {
        // Create sample adventure types
        String[] adventureTypeNames = {
            "Trekking", "Camping", "Water Sports", "Rock Climbing", "Paragliding",
            "River Rafting", "Scuba Diving", "Wildlife Safari", "Mountain Biking",
            "Skiing", "Snowboarding", "Bungee Jumping", "Zip Lining", "Cave Exploration",
            "Photography", "Bird Watching", "Backpacking", "Hiking"
        };

        for (String typeName : adventureTypeNames) {
            AdventureType adventureType = new AdventureType(typeName);
            adventureTypeRepository.save(adventureType);
        }
        
        System.out.println("Initialized " + adventureTypeNames.length + " adventure types");
    }

    private void initializeHiddenGems() {
        try {
            // Check if hidden gems already exist
            if (hiddenGemRepository.count() > 0) {
                System.out.println("Hidden gems already initialized, skipping...");
                return;
            }

            // Get states for associations
            State maharashtra = stateRepository.findByName("Maharashtra").orElse(null);
            State himachal = stateRepository.findByName("Himachal Pradesh").orElse(null);
            State uttarakhand = stateRepository.findByName("Uttarakhand").orElse(null);
            State kerala = stateRepository.findByName("Kerala").orElse(null);
            State rajasthan = stateRepository.findByName("Rajasthan").orElse(null);

            // Create hidden gems without adventure type associations for now
            if (maharashtra != null) {
                HiddenGem gem1 = new HiddenGem();
                gem1.setName("Harishchandragad Fort");
                gem1.setDescription("A magnificent hill fort known for its ancient caves, temples, and the famous Konkan Kada cliff. Perfect for night treks and camping under the stars.");
                gem1.setState(maharashtra);
                gem1.setLatitude(19.3833);
                gem1.setLongitude(73.7833);
                gem1.setNearestCity("Malshej Ghat");
                gem1.setBestTimeToVisit("October to March");
                gem1.setDifficultyLevel("Moderate to Difficult");
                gem1.setCostRange("₹500-1500 per person");
                gem1.setImageUrls(Set.of("https://example.com/harishchandragad1.jpg", "https://example.com/harishchandragad2.jpg"));
                hiddenGemRepository.save(gem1);
            }

            if (himachal != null) {
                HiddenGem gem2 = new HiddenGem();
                gem2.setName("Tosh Village");
                gem2.setDescription("A serene village in Parvati Valley, offering breathtaking mountain views, traditional Himachali culture, and excellent trekking opportunities.");
                gem2.setState(himachal);
                gem2.setLatitude(32.2396);
                gem2.setLongitude(77.3269);
                gem2.setNearestCity("Kasol");
                gem2.setBestTimeToVisit("April to June, September to November");
                gem2.setDifficultyLevel("Easy to Moderate");
                gem2.setCostRange("₹800-2000 per person");
                gem2.setImageUrls(Set.of("https://example.com/tosh1.jpg"));
                hiddenGemRepository.save(gem2);
            }

            if (uttarakhand != null) {
                HiddenGem gem3 = new HiddenGem();
                gem3.setName("Chopta Meadows");
                gem3.setDescription("Known as the 'Mini Switzerland of India', Chopta offers pristine meadows, rhododendron forests, and is the base for Tungnath trek.");
                gem3.setState(uttarakhand);
                gem3.setLatitude(30.4167);
                gem3.setLongitude(79.1167);
                gem3.setNearestCity("Rudraprayag");
                gem3.setBestTimeToVisit("April to June, September to November");
                gem3.setDifficultyLevel("Easy to Moderate");
                gem3.setCostRange("₹1000-2500 per person");
                gem3.setImageUrls(Set.of("https://example.com/chopta1.jpg", "https://example.com/chopta2.jpg"));
                hiddenGemRepository.save(gem3);
            }

            if (kerala != null) {
                HiddenGem gem4 = new HiddenGem();
                gem4.setName("Kumta Beach");
                gem4.setDescription("A pristine, less-crowded beach perfect for water sports, fishing, and watching spectacular sunsets. Rich in marine biodiversity.");
                gem4.setState(kerala);
                gem4.setLatitude(14.4167);
                gem4.setLongitude(74.4167);
                gem4.setNearestCity("Kumta");
                gem4.setBestTimeToVisit("October to March");
                gem4.setDifficultyLevel("Easy");
                gem4.setCostRange("₹300-800 per person");
                gem4.setImageUrls(Set.of("https://example.com/kumta1.jpg"));
                hiddenGemRepository.save(gem4);
            }

            if (rajasthan != null) {
                HiddenGem gem5 = new HiddenGem();
                gem5.setName("Khimsar Sand Dunes");
                gem5.setDescription("Lesser-known sand dunes offering authentic desert experience, camel safaris, and traditional Rajasthani culture away from crowded Jaisalmer.");
                gem5.setState(rajasthan);
                gem5.setLatitude(27.0333);
                gem5.setLongitude(73.0167);
                gem5.setNearestCity("Khimsar");
                gem5.setBestTimeToVisit("October to March");
                gem5.setDifficultyLevel("Easy");
                gem5.setCostRange("₹1500-3000 per person");
                gem5.setImageUrls(Set.of("https://example.com/khimsar1.jpg", "https://example.com/khimsar2.jpg"));
                hiddenGemRepository.save(gem5);
            }

            System.out.println("Initialized sample hidden gems (without adventure type associations)");
        } catch (Exception e) {
            System.err.println("Error initializing hidden gems: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
