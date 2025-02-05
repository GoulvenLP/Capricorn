/**
* Classe IRecordable.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.logger;

// TODO: Auto-generated Javadoc
/**
 * The Interface IRecordable.
 */
public interface IRecordable {
	
	/**
	 * Gets the titles.
	 *
	 * @return the titles
	 */
	/*
	 * Renvoie les entêtes correspondants aux données enregistrées => permet de donner un nom aux variables du records
	 */
	String[] getTitles();
	
	/**
	 * Gets the records.
	 *
	 * @return the records
	 */
	/*
	 * renvoie les données sous forme de chaines des données
	 */
	String[] getRecords();
	
	/**
	 * Gets the classement.
	 *
	 * @return the classement
	 */
	/*
	 * permet de catégoriser l'enregistrement. Sera utilisé pour classer les données enregistrées
	 */
	String getClassement();
}

