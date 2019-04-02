package core.jdbc.mysql;

public enum whereCriteria {
		/**
		 * =
		 */
		EQ,

		/**
		 * >
		 */
		GT,

		/**
		 * >=
		 */
		GE,

		/**
		 * <
		 */
		LT,

		/**
		 * <=
		 */
		LE,

		/**
		 * !=
		 */
		NE,

		/**
		 * IN
		 */
		IN,

		/**
		 * like
		 */
		LIKE,
		LIKE_LEFT,
		/**
		 * MATCH
		 */
		MATCH;
	
}
