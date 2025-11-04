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
        
        // Always call initializeHiddenGems as it has its own logic for handling existing data
        initializeHiddenGems();
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
            // Check if we have enough hidden gems (expecting 55+)
            long existingCount = hiddenGemRepository.count();
            if (existingCount >= 55) {
                System.out.println("Hidden gems already fully initialized (" + existingCount + " gems), skipping...");
                return;
            }
            
            // If we have some but not enough, clear and reinitialize all
            if (existingCount > 0 && existingCount < 55) {
                System.out.println("Found " + existingCount + " hidden gems, clearing and reinitializing with full dataset...");
                hiddenGemRepository.deleteAll();
            }

            // Get states for associations
            State maharashtra = stateRepository.findByName("Maharashtra").orElse(null);
            State himachal = stateRepository.findByName("Himachal Pradesh").orElse(null);
            State uttarakhand = stateRepository.findByName("Uttarakhand").orElse(null);
            State kerala = stateRepository.findByName("Kerala").orElse(null);
            State rajasthan = stateRepository.findByName("Rajasthan").orElse(null);
            State karnataka = stateRepository.findByName("Karnataka").orElse(null);
            State tamilNadu = stateRepository.findByName("Tamil Nadu").orElse(null);
            State goa = stateRepository.findByName("Goa").orElse(null);
            State gujarat = stateRepository.findByName("Gujarat").orElse(null);
            State madhyaPradesh = stateRepository.findByName("Madhya Pradesh").orElse(null);
            State jammuKashmir = stateRepository.findByName("Jammu and Kashmir").orElse(null);
            State ladakh = stateRepository.findByName("Ladakh").orElse(null);
            State sikkim = stateRepository.findByName("Sikkim").orElse(null);
            State meghalaya = stateRepository.findByName("Meghalaya").orElse(null);
            State assam = stateRepository.findByName("Assam").orElse(null);

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

            // Adding 50 more hidden gems across different states
            addMoreHiddenGems(maharashtra, himachal, uttarakhand, kerala, rajasthan, karnataka, 
                            tamilNadu, goa, gujarat, madhyaPradesh, jammuKashmir, ladakh, 
                            sikkim, meghalaya, assam);

            System.out.println("Initialized 55+ hidden gems across multiple states");
        } catch (Exception e) {
            System.err.println("Error initializing hidden gems: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addMoreHiddenGems(State maharashtra, State himachal, State uttarakhand, State kerala, 
                                  State rajasthan, State karnataka, State tamilNadu, State goa, 
                                  State gujarat, State madhyaPradesh, State jammuKashmir, State ladakh, 
                                  State sikkim, State meghalaya, State assam) {
        
        // Maharashtra (10 more gems)
        if (maharashtra != null) {
            addHiddenGem("Kalsubai Peak", "Highest peak in Maharashtra offering stunning sunrise views and challenging trek through dense forests.", maharashtra, 19.6, 73.7, "Igatpuri", "October to February", "Moderate to Difficult", "₹400-1000 per person");
            addHiddenGem("Bhandardara", "Serene hill station with pristine lake, waterfalls, and the famous Wilson Dam. Perfect for camping and stargazing.", maharashtra, 19.55, 73.75, "Ahmednagar", "June to February", "Easy", "₹800-2000 per person");
            addHiddenGem("Kaas Plateau", "UNESCO World Heritage site known as the Valley of Flowers of Maharashtra, blooming with endemic wildflowers.", maharashtra, 17.72, 73.82, "Satara", "August to October", "Easy", "₹300-600 per person");
            addHiddenGem("Sandhan Valley", "The Grand Canyon of Maharashtra offering rappelling, rock climbing, and narrow gorge exploration.", maharashtra, 19.37, 73.78, "Samrad Village", "October to March", "Difficult", "₹1000-2500 per person");
            addHiddenGem("Ratangad Fort", "Ancient fort with natural rock-cut caves and spectacular views of Sahyadri ranges. Famous for its needle-like pinnacle.", maharashtra, 19.4, 73.78, "Ratanwadi", "October to March", "Moderate", "₹400-800 per person");
            addHiddenGem("Torna Fort", "First fort captured by Chhatrapati Shivaji, offering historical significance and panoramic valley views.", maharashtra, 18.24, 73.62, "Velhe", "October to March", "Moderate", "₹300-700 per person");
            addHiddenGem("Devkund Waterfall", "Hidden waterfall in dense forest accessible only by trekking, forming a beautiful natural pool.", maharashtra, 18.33, 73.45, "Tamhini", "June to September", "Moderate", "₹500-1200 per person");
            addHiddenGem("Kokankada", "Spectacular cliff edge offering breathtaking views of Konkan coast and Arabian Sea from Western Ghats.", maharashtra, 18.45, 73.5, "Lonavala", "October to March", "Easy to Moderate", "₹200-500 per person");
            addHiddenGem("Visapur Fort", "Twin fort of Lohagad offering easier trek with impressive architecture and monsoon waterfalls.", maharashtra, 18.76, 73.44, "Malavli", "October to March", "Easy to Moderate", "₹300-600 per person");
            addHiddenGem("Andharban Trek", "Dense forest trek meaning 'Dark Forest' leading to beautiful waterfalls and valley views.", maharashtra, 18.4, 73.4, "Pimpri", "June to September", "Moderate", "₹600-1500 per person");
        }

        // Himachal Pradesh (8 more gems)
        if (himachal != null) {
            addHiddenGem("Barot Valley", "Untouched valley perfect for trout fishing, camping, and exploring traditional Himachali villages.", himachal, 31.85, 76.75, "Mandi", "March to June, September to November", "Easy", "₹1000-2500 per person");
            addHiddenGem("Shoja Village", "Offbeat destination in Seraj Valley offering apple orchards, dense forests, and pristine mountain views.", himachal, 31.75, 77.35, "Banjar", "April to June, September to November", "Easy", "₹800-2000 per person");
            addHiddenGem("Chitkul", "Last inhabited village near Indo-China border offering stunning Kinnaur Kailash views and traditional culture.", himachal, 31.35, 78.43, "Sangla", "April to October", "Easy to Moderate", "₹1200-3000 per person");
            addHiddenGem("Prashar Lake", "Sacred high-altitude lake with floating island and ancient temple, offering 360-degree mountain views.", himachal, 31.75, 77.15, "Mandi", "April to November", "Moderate", "₹800-2000 per person");
            addHiddenGem("Jibhi Waterfall", "Hidden waterfall in Tirthan Valley accessible through beautiful forest trail and traditional villages.", himachal, 31.55, 77.35, "Banjar", "March to June, September to November", "Easy", "₹600-1500 per person");
            addHiddenGem("Kalpa", "Scenic village offering stunning Kinnaur Kailash views, apple orchards, and traditional Kinnauri architecture.", himachal, 31.53, 78.25, "Reckong Peo", "April to October", "Easy", "₹1000-2500 per person");
            addHiddenGem("Narkanda", "Offbeat hill station perfect for skiing, apple orchards, and Hatu Peak trek with panoramic Himalayan views.", himachal, 31.28, 77.37, "Shimla", "December to March (skiing), April to June", "Easy to Moderate", "₹1200-3000 per person");
            addHiddenGem("Bir Billing", "World's second-best paragliding site offering adventure sports and Tibetan monasteries in serene valley.", himachal, 32.05, 76.73, "Kangra", "March to June, September to November", "Easy", "₹2000-5000 per person");
        }

        // Uttarakhand (8 more gems)
        if (uttarakhand != null) {
            addHiddenGem("Munsiyari", "Base camp for Milam and Ralam glaciers offering stunning Panchachuli peaks views and traditional Kumaoni culture.", uttarakhand, 30.07, 80.24, "Pithoragarh", "April to June, September to November", "Moderate", "₹1500-3500 per person");
            addHiddenGem("Kausani", "Hill station offering 300km panoramic view of Himalayan peaks including Nanda Devi and Trishul.", uttarakhand, 29.84, 79.61, "Almora", "March to June, September to November", "Easy", "₹800-2000 per person");
            addHiddenGem("Abbott Mount", "Colonial-era hill station with heritage cottages, oak forests, and stunning Himalayan views.", uttarakhand, 29.45, 80.07, "Champawat", "March to June, September to November", "Easy", "₹1000-2500 per person");
            addHiddenGem("Binsar Wildlife Sanctuary", "Dense oak and rhododendron forests offering 300km Himalayan views and diverse wildlife.", uttarakhand, 29.68, 79.73, "Almora", "March to June, September to November", "Easy to Moderate", "₹600-1500 per person");
            addHiddenGem("Chakrata", "Cantonment town offering Tiger Falls, Deoban forests, and adventure activities away from crowds.", uttarakhand, 30.7, 77.87, "Dehradun", "March to June, September to November", "Easy", "₹800-2000 per person");
            addHiddenGem("Kanatal", "Serene hill station offering apple orchards, dense forests, and stunning views of Garhwal Himalayas.", uttarakhand, 30.42, 78.32, "Tehri", "March to June, September to November", "Easy", "₹1000-2500 per person");
            addHiddenGem("Pithoragarh", "Gateway to Kailash Mansarovar offering ancient temples, forts, and stunning valley views.", uttarakhand, 29.58, 80.21, "Pithoragarh", "March to June, September to November", "Easy", "₹800-2000 per person");
            addHiddenGem("Lansdowne", "Quiet cantonment town offering colonial architecture, oak forests, and peaceful mountain atmosphere.", uttarakhand, 29.84, 78.69, "Pauri", "March to June, September to November", "Easy", "₹800-2000 per person");
        }

        // Kerala (6 more gems)
        if (kerala != null) {
            addHiddenGem("Vagamon", "Hill station with rolling meadows, pine forests, and adventure activities like paragliding and rock climbing.", kerala, 9.7, 76.9, "Kottayam", "September to March", "Easy", "₹1000-2500 per person");
            addHiddenGem("Bekal Fort Beach", "Pristine beach with well-preserved 17th-century fort offering stunning Arabian Sea views.", kerala, 12.39, 75.03, "Kasaragod", "October to March", "Easy", "₹500-1200 per person");
            addHiddenGem("Nelliampathy", "Hill station with tea and coffee plantations, orange groves, and scenic valley views.", kerala, 10.53, 76.68, "Palakkad", "September to March", "Easy", "₹800-2000 per person");
            addHiddenGem("Ponmudi", "Golden peak hill station offering mist-covered mountains, tea gardens, and trekking trails.", kerala, 8.76, 77.11, "Thiruvananthapuram", "September to March", "Easy to Moderate", "₹600-1500 per person");
            addHiddenGem("Marari Beach", "Secluded beach village offering pristine coastline, coconut groves, and traditional fishing culture.", kerala, 9.6, 76.15, "Alappuzha", "October to March", "Easy", "₹800-2000 per person");
            addHiddenGem("Silent Valley", "UNESCO Biosphere Reserve with pristine rainforest, endemic species, and untouched wilderness.", kerala, 11.09, 76.43, "Palakkad", "October to March", "Moderate", "₹1000-2500 per person");
        }

        // Rajasthan (6 more gems)
        if (rajasthan != null) {
            addHiddenGem("Bhangarh Fort", "Haunted archaeological site with mysterious legends, ancient temples, and historical significance.", rajasthan, 27.07, 76.0, "Alwar", "October to March", "Easy", "₹200-500 per person");
            addHiddenGem("Kumbhalgarh Fort", "Massive fort with 36km wall, second longest in world, offering spectacular sunset views.", rajasthan, 25.15, 73.58, "Rajsamand", "October to March", "Moderate", "₹400-1000 per person");
            addHiddenGem("Osian", "Ancient town with beautiful Jain and Hindu temples, offering camel safaris and desert camping.", rajasthan, 26.73, 72.93, "Jodhpur", "October to March", "Easy", "₹1500-3500 per person");
            addHiddenGem("Ranakpur", "Stunning 15th-century Jain temple complex with intricate marble carvings and 1444 unique pillars.", rajasthan, 25.12, 73.47, "Pali", "October to March", "Easy", "₹300-700 per person");
            addHiddenGem("Mandawa", "Open-air art gallery town with beautifully painted havelis showcasing traditional Shekhawati frescoes.", rajasthan, 28.06, 75.14, "Jhunjhunu", "October to March", "Easy", "₹800-2000 per person");
            addHiddenGem("Barmer", "Desert town famous for handicrafts, folk music, and authentic Rajasthani culture away from tourist crowds.", rajasthan, 25.75, 71.4, "Barmer", "October to March", "Easy", "₹1000-2500 per person");
        }

        // Karnataka (4 more gems)
        if (karnataka != null) {
            addHiddenGem("Agumbe", "Rainforest research station known as Cherrapunji of South India, offering sunset views and biodiversity.", karnataka, 13.51, 75.1, "Shimoga", "October to March", "Easy", "₹600-1500 per person");
            addHiddenGem("Yana Rocks", "Unique black limestone rock formations in dense forest, perfect for rock climbing and cave exploration.", karnataka, 14.55, 74.37, "Kumta", "October to March", "Moderate", "₹500-1200 per person");
            addHiddenGem("Kudremukh", "Horse-face shaped peak in Western Ghats offering challenging trek through grasslands and forests.", karnataka, 13.15, 75.15, "Chikmagalur", "October to March", "Moderate to Difficult", "₹800-2000 per person");
            addHiddenGem("Sakleshpur", "Hill station with coffee plantations, railway bridge walks, and beautiful Western Ghats scenery.", karnataka, 12.94, 75.78, "Hassan", "October to March", "Easy", "₹800-2000 per person");
        }

        // Tamil Nadu (3 more gems)
        if (tamilNadu != null) {
            addHiddenGem("Yelagiri", "Lesser-known hill station offering adventure activities, rose gardens, and peaceful mountain atmosphere.", tamilNadu, 12.63, 78.65, "Vellore", "October to March", "Easy", "₹600-1500 per person");
            addHiddenGem("Kolli Hills", "Ancient hills with 70 hairpin bends, waterfalls, and medicinal plants mentioned in Tamil literature.", tamilNadu, 11.37, 78.33, "Namakkal", "October to March", "Moderate", "₹500-1200 per person");
            addHiddenGem("Tranquebar", "Danish colonial town with heritage buildings, pristine beach, and unique Indo-European architecture.", tamilNadu, 11.03, 79.85, "Nagapattinam", "October to March", "Easy", "₹400-1000 per person");
        }

        // Gujarat (3 more gems)
        if (gujarat != null) {
            addHiddenGem("Champaner", "UNESCO World Heritage archaeological park with Indo-Islamic architecture and historical significance.", gujarat, 22.48, 73.53, "Vadodara", "October to March", "Easy", "₹300-700 per person");
            addHiddenGem("Saputara", "Only hill station in Gujarat offering tribal culture, waterfalls, and adventure activities.", gujarat, 20.57, 73.75, "Surat", "October to March", "Easy", "₹800-2000 per person");
            addHiddenGem("Mandvi Beach", "Pristine beach with white sand, wind farms, and 18th-century Vijay Vilas Palace.", gujarat, 22.84, 69.35, "Bhuj", "October to March", "Easy", "₹600-1500 per person");
        }

        // Goa (2 more gems)
        if (goa != null) {
            addHiddenGem("Netravali Wildlife Sanctuary", "Hidden sanctuary with bubble lake, waterfalls, and diverse wildlife away from beaches.", goa, 15.25, 74.12, "Sanguem", "October to March", "Easy", "₹400-1000 per person");
            addHiddenGem("Divar Island", "Peaceful island accessible by ferry offering Portuguese heritage, churches, and rural Goan life.", goa, 15.53, 73.92, "Old Goa", "October to March", "Easy", "₹300-800 per person");
        }

        // Ladakh (2 more gems)
        if (ladakh != null) {
            addHiddenGem("Turtuk Village", "Last village before Pakistan border offering Balti culture, apricot orchards, and stunning mountain views.", ladakh, 34.85, 76.83, "Nubra Valley", "May to September", "Easy", "₹2000-4000 per person");
            addHiddenGem("Hanle", "World's highest astronomical observatory offering dark skies, stargazing, and remote Himalayan beauty.", ladakh, 32.78, 78.97, "Leh", "May to September", "Moderate", "₹2500-5000 per person");
        }

        // Sikkim (2 more gems)
        if (sikkim != null) {
            addHiddenGem("Zuluk", "Zigzag mountain road offering stunning sunrise views over Kanchenjunga and historic Silk Route.", sikkim, 27.12, 88.72, "Gangtok", "March to May, October to December", "Easy", "₹1500-3000 per person");
            addHiddenGem("Ravangla", "Peaceful town offering Buddha Park, monasteries, and panoramic views of snow-capped peaks.", sikkim, 27.32, 88.62, "Namchi", "March to May, October to December", "Easy", "₹1000-2500 per person");
        }

        // Meghalaya (2 more gems)
        if (meghalaya != null) {
            addHiddenGem("Mawlynnong", "Asia's cleanest village offering living root bridges, tree houses, and sustainable tourism practices.", meghalaya, 25.18, 91.88, "Shillong", "October to April", "Easy", "₹800-2000 per person");
            addHiddenGem("Dawki", "Crystal clear Umngot river offering boating, camping, and stunning views of Bangladesh border.", meghalaya, 25.12, 92.02, "Shillong", "October to April", "Easy", "₹600-1500 per person");
        }

        // Assam (2 more gems)
        if (assam != null) {
            addHiddenGem("Majuli Island", "World's largest river island offering Vaishnavite monasteries, traditional crafts, and unique culture.", assam, 27.0, 94.22, "Jorhat", "October to April", "Easy", "₹800-2000 per person");
            addHiddenGem("Haflong", "Only hill station in Assam offering lakes, waterfalls, and tribal culture of Dima Hasao district.", assam, 25.17, 93.02, "Silchar", "October to April", "Easy", "₹1000-2500 per person");
        }

        // Madhya Pradesh (2 more gems)
        if (madhyaPradesh != null) {
            addHiddenGem("Bhimbetka", "UNESCO World Heritage site with ancient rock paintings dating back 30,000 years.", madhyaPradesh, 22.95, 77.6, "Bhopal", "October to March", "Easy", "₹200-500 per person");
            addHiddenGem("Pachmarhi", "Only hill station in MP offering waterfalls, caves, and colonial architecture in Satpura range.", madhyaPradesh, 22.47, 78.43, "Hoshangabad", "October to March", "Easy", "₹800-2000 per person");
        }

        // Jammu and Kashmir (1 more gem)
        if (jammuKashmir != null) {
            addHiddenGem("Betaab Valley", "Pristine valley named after Bollywood film, offering meadows, streams, and snow-capped peaks.", jammuKashmir, 34.01, 75.28, "Pahalgam", "April to October", "Easy", "₹1000-2500 per person");
        }
    }

    private void addHiddenGem(String name, String description, State state, double latitude, double longitude, 
                             String nearestCity, String bestTime, String difficulty, String costRange) {
        HiddenGem gem = new HiddenGem();
        gem.setName(name);
        gem.setDescription(description);
        gem.setState(state);
        gem.setLatitude(latitude);
        gem.setLongitude(longitude);
        gem.setNearestCity(nearestCity);
        gem.setBestTimeToVisit(bestTime);
        gem.setDifficultyLevel(difficulty);
        gem.setCostRange(costRange);
        gem.setImageUrls(Set.of("https://example.com/" + name.toLowerCase().replace(" ", "_") + ".jpg"));
        hiddenGemRepository.save(gem);
    }
}
