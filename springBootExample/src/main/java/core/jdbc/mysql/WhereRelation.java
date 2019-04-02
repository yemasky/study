package core.jdbc.mysql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WhereRelation {
	/**
	 * =
	 */
	public final String EQ = "=";

	/**
	 * >
	 */
	public final String GT = ">";

	/**
	 * >=
	 */
	public final String GE = ">=";

	/**
	 * <
	 */
	public final String LT = "<";

	/**
	 * <=
	 */
	public final String LE = "<=";

	/**
	 * !=
	 */
	public final String NE = "!=";

	/**
	 * IN
	 */
	public final String IN = "IN";

	/**
	 * like
	 */
	public final String LIKE = "LIKE";
	public final String LIKE_LEFT = "LIKE";
	
	/**
	 * MATCH
	 */
	public final String MATCH = "MATCH";
	
	private String where = "";
	private String order = "";
	private String group = "";
	private String limit = "";
	private Map<whereCriteria, HashMap<String, Object>> whereMap = new HashMap<whereCriteria, HashMap<String, Object>>();
	private Object[] whereParamters = null;
	private static HashMap<String, WhereRelation> whereRelationInstance = new HashMap<>();

	public static WhereRelation instance() {
		return new WhereRelation();
	}
	
	public static WhereRelation instance(String key) {
		WhereRelation whereCriteria = null;
		if(!whereRelationInstance.containsKey(key)) {
			whereCriteria = new WhereRelation();
			whereRelationInstance.put(key, whereCriteria);
		} else {
			whereCriteria = whereRelationInstance.get(key);
		}
		whereCriteria.emptyParameters();
		return whereCriteria;
	}
	
	public WhereRelation emptyParameters() {
		this.where = "";
		this.order = "";
		this.group = "";
		this.limit = "";
		this.whereMap = new HashMap<whereCriteria, HashMap<String, Object>>();
		this.whereParamters = null;
		return this;
	}
	public WhereRelation where(String whereSQL) {
		if (this.where.startsWith(" WHERE")) {
			this.where = this.where + " AND " + whereSQL;
		} else {
			this.where = " WHERE " + whereSQL;
		}
		return this;
	}
	
	public WhereRelation where(HashMap<String, Object> whereCondition) {
		if (!this.whereMap.containsKey(whereCriteria.EQ)) {
			this.whereMap.put(whereCriteria.EQ, new HashMap<String, Object>());
		}
		this.whereMap.get(whereCriteria.EQ).putAll(whereCondition);
		return this;
	}
	
	public WhereRelation where(whereCriteria criteria, HashMap<String, Object> whereCondition) throws SQLException {
		if (!this.whereMap.containsKey(criteria)) {
			this.whereMap.put(criteria, new HashMap<String, Object>());
		}
		this.whereMap.get(criteria).putAll(whereCondition);
		return this;
	}
	
	public WhereRelation where(Map<whereCriteria, HashMap<String, Object>> whereCondition) throws SQLException {
		for (whereCriteria criteria : whereCondition.keySet()) {
			this.where(criteria, whereCondition.get(criteria));
		}
		return this;
	}

	public WhereRelation group(String groupSQL) {
		if (this.group.startsWith(" GROUP BY")) {
			this.group = this.group + ", " + groupSQL;
		} else {
			this.group = " GROUP BY " + groupSQL;
		}
		return this;
	}

	public WhereRelation order(String orderSQL) {
		if (this.order.startsWith(" ORDER BY")) {
			this.order = this.order + ", " + orderSQL;
		} else {
			this.order = " ORDER BY " + orderSQL;
		}
		return this;
	}

	public WhereRelation Limit(int offset, int rows) {
		this.setLimit(" LIMIT " + offset + ", " + rows + ";");
		return this;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}
	
	public String getWhere() {
		StringBuilder whereSQL = new StringBuilder("");
		List<Object> whereParamtersList = new ArrayList<>();
		if(!this.whereMap.isEmpty() && this.whereMap.size() > 0) {
			Map<whereCriteria, HashMap<String, Object>> whereHashMap = this.whereMap;
			String conditional = "";
			for(Entry<whereCriteria, HashMap<String, Object>> criteria : whereHashMap.entrySet()) {
				HashMap<String, Object> valueObjectMap = criteria.getValue();
				switch (criteria.getKey()) {
				case EQ://=
					conditional = this.EQ;
					for (Entry<String, Object> valueRelation : valueObjectMap.entrySet()) {
						whereSQL.append(" AND " + valueRelation.getKey() + " " + conditional + " ? ");
						whereParamtersList.add(valueRelation.getValue());
					}
					break;
				case GT://>
					conditional = this.GT;
					for (Entry<String, Object> valueRelation : valueObjectMap.entrySet()) {
						whereSQL.append(" AND " + valueRelation.getKey() + " " + conditional + " ? ");
						whereParamtersList.add(valueRelation.getValue());
					}
					break;
				case GE://>=
					conditional = this.GE;
					for (Entry<String, Object> valueRelation : valueObjectMap.entrySet()) {
						whereSQL.append(" AND " + valueRelation.getKey() + " " + conditional + " ? ");
						whereParamtersList.add(valueRelation.getValue());
					}
					break;
				case LT://<
					conditional = this.LT;
					for (Entry<String, Object> valueRelation : valueObjectMap.entrySet()) {
						whereSQL.append(" AND " + valueRelation.getKey() + " " + conditional + " ? ");
						whereParamtersList.add(valueRelation.getValue());
					}
					break;
				case LE://<=
					conditional = this.LE;
					for (Entry<String, Object> valueRelation : valueObjectMap.entrySet()) {
						whereSQL.append(" AND " + valueRelation.getKey() + " " + conditional + " ? ");
						whereParamtersList.add(valueRelation.getValue());
					}
					break;
				case NE://!=
					conditional = this.NE;
					for (Entry<String, Object> valueRelation : valueObjectMap.entrySet()) {
						whereSQL.append(" AND " + valueRelation.getKey() + " " + conditional + " ? ");
						whereParamtersList.add(valueRelation.getValue());
					}
					break;
				case IN://IN
					conditional = this.IN;
					for (Entry<String, Object> valueRelation : valueObjectMap.entrySet()) {
						Object value = valueRelation.getValue();
						String inStrSql = "?";
						if(value.getClass().isArray()) {//数组
							//字符串数组
							int valueSize = 0;
							String[] inStr = null;
							//if(value.getClass().getName().contains("String")) {
							if (value.getClass().getName().contains("[I")) {
								int[] arrayValue = (int[]) value;
								valueSize = arrayValue.length;
								inStr = new String[valueSize];
								for(int i = 0; i < valueSize; i++ ) {
									inStr[i] = "?";
									whereParamtersList.add(arrayValue[i]);
								}
							} else {
								Object[] arrayValue = (Object[]) value;
								valueSize = arrayValue.length;
								inStr = new String[valueSize];
								for(int i = 0; i < valueSize; i++ ) {
									inStr[i] = "?";
									whereParamtersList.add(arrayValue[i]);
								}
							}
							inStrSql = Arrays.toString(inStr);
							inStrSql = inStrSql.substring(1, inStrSql.length()-1);
						} else {
							whereParamtersList.add(value);
						}
						whereSQL.append(" AND " + valueRelation.getKey() + " IN("+inStrSql+") ");
					}
					break;
				case LIKE://LIKE
					for (Entry<String, Object> valueRelation : valueObjectMap.entrySet()) {
						whereSQL.append(" AND " + valueRelation.getKey() + " " + " LIKE('%?%') ");
						whereParamtersList.add(valueRelation.getValue());
					}
					break;
				case LIKE_LEFT://LIKE
					for (Entry<String, Object> valueRelation : valueObjectMap.entrySet()) {
						whereSQL.append(" AND " + valueRelation.getKey() + " " + " LIKE('?%') ");
						whereParamtersList.add(valueRelation.getValue());
					}
					break;
				case MATCH://MATCH
					for (Entry<String, Object> valueRelation : valueObjectMap.entrySet()) {
						whereSQL.append(" AND MATCH (" + valueRelation.getKey() + ") AGAINST (?) ");
						whereParamtersList.add(valueRelation.getValue());
					}
					break;
				default:
					break;
					
				}
			}
			this.whereParamters = whereParamtersList.toArray();
			if (this.where.startsWith(" WHERE")) {
				return this.where + whereSQL.toString() + this.order + this.group + this.limit + ";";
			}
			return " WHERE " + whereSQL.toString().substring(4) + this.order + this.group + this.limit + ";";
		}
		return this.where + this.order + this.group + this.limit + ";";
	}

	public Object[] getWhereParamters() {
		return whereParamters;
	}
	
}
