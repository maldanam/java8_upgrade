package got;

import java.util.Collection;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

public class MemberDAOTests {
    private Collection<Member> allMembers = InMemoryMemberDAO.getInstance().getAll();

    /**
     * Find all members whose name starts with S and sort by id (natural sort)
     */
    @Test
    public void startWithS_sortByAlpha() {
    	System.out.println("--> Find all members whose name starts with S and sort by id (natural sort)");
    	allMembers.stream()
    			  .filter(m -> m.getName().startsWith("S"))
    			  .sorted()
    			  .forEach(System.out::println);
    }

    /**
     * Final all Starks and sort them by name
     */
    @Test
    public void starks_alphaByName() {
    	System.out.println("--> Find all Starks and sort them by name");
    	allMembers.stream()
    			  .filter(m -> "Stark".equals(m.getHouseName()))
    			  .sorted((m1, m2) -> m1.getName().compareTo(m2.getName()))
    			  .forEach(System.out::println);
    }

    /**
     * Find all members whose salary is less than 80K and sort by house
     */
    @Test
    public void salaryLessThan_sortByHouseName() {
    	System.out.println("--> Find all members whose salary is less than 80K and sort by house");
    	allMembers.stream()
		  .filter(m -> m.getSalary() < 80_000)
		  .sorted((m1, m2) -> m1.getHouseName().compareTo(m2.getHouseName()))
		  .forEach(System.out::println);
    }

    /**
     * Sort members by house name, then by name
     */
    @Test
    public void sortByHouseName_sortByNameDesc() {
    	System.out.println("--> Sort members by house name, then by name");
    	allMembers.stream()
    	  .sorted(Comparator.comparing(Member::getHouseName).thenComparing(Member::getName))
//		  .sorted((m1, m2) -> m1.getHouseName().equals(m2.getHouseName()) ? m2.getName().compareTo(m1.getName()) : m1.getHouseName().compareTo(m2.getHouseName()))
		  .forEach(System.out::println);
    }

    /**
     * Sort the Starks by birthdate
     */
    @Test
    public void starksByDob() {
    	System.out.println("--> Sort the Starks by birthdate");
    	allMembers.stream()
		  .filter(m -> "Stark".equals(m.getHouseName()))
		  .sorted((m1, m2) -> m1.getDob().compareTo(m2.getDob()))
		  .forEach(System.out::println);
    }

    /**
     * Find all Kings and sort by name in descending order
     */
    @Test
    public void kingsByNameDesc() {
    	System.out.println("--> Find all Kings and sort by name in descending order");
    	allMembers.stream()
		  .filter(m -> Title.KING.equals(m.getTitle()))
		  .sorted((m1, m2) -> m2.getName().compareTo(m1.getName()))
		  .forEach(System.out::println);
    }

    /**
     * Find the average salary
     */
    @Test
    public void averageSalary() {
    	System.out.println("--> Find the average salary");
    	allMembers.stream()
    	  .mapToDouble(m -> m.getSalary())
    	  .average()
		  .ifPresent(System.out::println);
    }

    /**
     * Get the names of all the Starks, sorted in natural order
     * (note _names_, not members)
     */
    @Test
    public void namesSorted() {
    	System.out.println("--> Get the names of all the Starks, sorted in natural order");
    	allMembers.stream()
		  .filter(m -> "Stark".equals(m.getHouseName()))
		  .sorted()
		  .map(Member::getName)
		  .forEach(System.out::println);
    }

    /**
     * Are all the salaries greater than 100K?
     */
    @Test
    public void salariesGT100k() {
    	System.out.println("--> Are all the salaries greater than 100K?");
    	System.out.println(allMembers.stream()
		  .filter(m -> m.getSalary() <= 100_000)
		  .count() == 0);
    }

    /**
     * Are there any members of House Greyjoy?
     */
    @Test
    public void greyjoys() {
    	System.out.println("--> Are there any members of House Greyjoy?");
    	System.out.println(allMembers.stream()
    	  .filter(m -> "Greyjoy".equals(m.getHouseName()))
		  .count() > 0);
    }

    /**
     * How many Lannisters are there?
     */
    @Test
    public void howManyLannisters() {
    	System.out.println("--> How many Lannisters are there?");
    	System.out.println(allMembers.stream()
    	    	  .filter(m -> "Lannister".equals(m.getHouseName()))
    			  .count());
    }

    /**
     * Print the names of any three Lannisters
     */
    @Test
    public void threeLannisters() {
    	System.out.println("--> Print the names of any three Lannisters");
    	allMembers.stream()
  	    	  .filter(m -> "Lannister".equals(m.getHouseName()))
  			  .limit(3)
  			  .forEach(System.out::println);;
    }

    /**
     * Print the names of the Lannisters as a comma-separated string
     */
    @Test
    public void lannisterNames() {
    	System.out.println("--> Print the names of the Lannisters as a comma-separated string");
    	System.out.println(allMembers.stream()
    	  .filter(m -> "Lannister".equals(m.getHouseName()))
    	  .map(m -> m.getName())
//    	  .peek(System.out::println)
    	  .collect(Collectors.joining(", ")));
    }

    /**
     * Who has the highest salary?
     */
    @Test
    public void highestSalary() {
    	System.out.println("--> Who has the highest salary?");
    	allMembers.stream()
    			  .max((m1, m2) -> Double.compare(m1.getSalary(), m2.getSalary()))
    			  .ifPresent(System.out::println);
    }

    /**
     * Partition members into male and female
     * (note: women are LADY or QUEEN, men are everything else)
     */
    @Test
    public void menVsWomen() {
    	System.out.println("--> Partition members into male and female");
    	Map<String, List<Member>> maleOrFemale = allMembers.stream()
    			  .collect(Collectors.groupingBy(m -> (Title.LADY.equals(m.getTitle()) || Title.QUEEN.equals(m.getTitle())) ? "Women" : "Men"));

    	System.out.println("Males: " + maleOrFemale.get("Men"));
    	System.out.println("Females: " + maleOrFemale.get("Women"));
    }

    /**
     * Group members into Houses
     */
    @Test
    public void membersByHouse() {
    	System.out.println("--> Group members into Houses");
    	Map<House, List<Member>> byHouse = allMembers.stream()
		  .collect(Collectors.groupingBy(Member::getHouse));
    	
    	byHouse.keySet().stream().forEach(h -> System.out.println(h + ": " + byHouse.get(h)));
    }

    /**
     * How many members are in each house?
     * (group by house, downstream collector using count
     */
    @Test
    public void numberOfMembersByHouse() {
    	System.out.println("--> How many members are in each house?");
    	Map<House, List<Member>> byHouse = allMembers.stream()
		  .collect(Collectors.groupingBy(Member::getHouse));
    	
    	byHouse.keySet().stream().forEach(h -> System.out.println(h + ": " + byHouse.get(h).size()));
    }

    /**
     * Get the max, min, and ave salary for each house
     */
    @Test
    public void houseStats() {
    	System.out.println("--> Get the max, min, and ave salary for each house");
    	Map<House, List<Member>> byHouse = allMembers.stream()
		  .collect(Collectors.groupingBy(Member::getHouse));

    	byHouse.keySet().stream()
    					.forEach(h -> { 
    						DoubleSummaryStatistics stats = byHouse.get(h).stream().collect(Collectors.summarizingDouble(Member::getSalary));
    						System.out.println(String.format("%s: maxSalary: %s minSalary: %s averageSalary: %s", h, stats.getMax(), stats.getMin(), stats.getAverage())); 
    					});
    }
}
