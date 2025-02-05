/*
 * 
 */
package enstabretagne.base.messages;

import enstabretagne.base.logger.ToRecord;

// TODO: Auto-generated Javadoc
/**
 * The Class MessagesLogger.
 */
public class MessagesLogger {

	/** The Constant LoggerImpossible. */
	public static final String LoggerImpossible = "Ne devrait pas arriver";

	/** The Constant LoggerSettingsDirNotFound. */
	public static final String LoggerSettingsDirNotFound = "Répertoire de configuration du logger non trouvé";
	
	/** The Constant LoggerSettingsFileNotFound. */
	public static final String LoggerSettingsFileNotFound = "Fichier de configuration du logger non trouvé";
	
	/** The Constant LoggerKindNotProvided. */
	public static final String LoggerKindNotProvided = "Type de logger non fourni";
	
	/** The Constant LoggerKindNotFound. */
	public static final String LoggerKindNotFound = "Type de logger non trouvé";
	
	/** The Constant LoggerParsingFailed. */
	public static final String LoggerParsingFailed = "Erreur de parsing du JSON";

	/** The Constant LoggerDataIncorrectMethod. */
	public static final String LoggerDataIncorrectMethod = "La méthode suivante devrait être avoir une signature de getter comme 'Object fonc()'";
	
	/** The Constant LoggerDataReuseOfAToRecordName. */
	public static final String LoggerDataReuseOfAToRecordName = "Nom déjà donné à une méthode par l'annotation '"+ToRecord.class.getName()+"'";

	/** The Constant ExcelLoggerDirNotFound. */
	public static final String ExcelLoggerDirNotFound = "Nom du Répertoire cible non trouvé";
	
	/** The Constant ExcelLoggerFileNotFound. */
	public static final String ExcelLoggerFileNotFound = "Nom du Fichier cible non trouvé";
	
	/** The Constant ExcelLoggerRecordStartTimeNotFound. */
	public static final String ExcelLoggerRecordStartTimeNotFound = "Heure de début d'enregistrement non trouvée";
}
