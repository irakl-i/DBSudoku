package assign3;

public class Entry {
	private String metropolis;
	private String continent;
	private int population;

	/**
	 * Constructor.
	 * @param metropolis
	 * @param continent
	 * @param population
	 */
	public Entry(String metropolis, String continent, int population) {
		this.metropolis = metropolis;
		this.continent = continent;
		this.population = population;
	}

	/**
	 * Returns metropolis.
	 * @return metropolis
	 */
	public String getMetropolis() {
		return metropolis;
	}

	/**
	 * Returns continent.
	 * @return continent
	 */
	public String getContinent() {
		return continent;
	}

	/**
	 * Returns population.
	 * @return population
	 */
	public int getPopulation() {
		return population;
	}

	@Override
	public String toString() {
		return metropolis + "\t" + continent + "\t" + population;
	}
}
