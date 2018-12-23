
public class UpdateFromBusinessTO {
	
	Long id;
	Integer numberOFPeople;
	Integer numberOFPeopleThatEnter;
	Integer numberOFPeopleThatExit;
	Integer date;
	String confirmation;
	
	public UpdateFromBusinessTO() {

	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumberOFPeople() {
		return numberOFPeople;
	}

	public void setNumberOFPeople(Integer numberOFPeople) {
		this.numberOFPeople = numberOFPeople;
	}

	public Integer getNumberOFPeopleThatEnter() {
		return numberOFPeopleThatEnter;
	}

	public void setNumberOFPeopleThatEnter(Integer numberOFPeopleThatEnter) {
		this.numberOFPeopleThatEnter = numberOFPeopleThatEnter;
	}

	public Integer getNumberOFPeopleThatExit() {
		return numberOFPeopleThatExit;
	}

	public void setNumberOFPeopleThatExit(Integer numberOFPeopleThatExit) {
		this.numberOFPeopleThatExit = numberOFPeopleThatExit;
	}

	public Integer getDate() {
		return date;
	}

	public void setDate(Integer date) {
		this.date = date;
	}

	public String getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}
	
}
