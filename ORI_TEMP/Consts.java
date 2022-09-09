/* Consts.java
 * -----------------
 * Usage: To store constant values and enumerations
 */
public class Consts {

	static final long SERIAL_VERSION_UID = 1L;

    static final String[] OPTIONS = { "Businesses Details", "Business Locations", "Business Accessibility", "Business Hours", "Business Parking", "Customer Details", "Customer Reviews" };
    static final String[] OUTPUT = { "Text", "File"};

    static final int NUM_OPTIONS = OPTIONS.length;
    static final int NUM_OUTPUTS = OUTPUT.length;

		// Strings representing the SQL statements for each option in OPTIONS, respectively:
		static final String[] SQL_STATEMENTS = {
			"SELECT business_id, name, stars, review_count FROM service",
			"SELECT service.name, location.address, location.city, location.state, location.postal_code FROM service INNER JOIN location ON service.business_id = location.business_id",
			"SELECT service.name, attributes.bikeparking, attributes.goodforkids, attributes.byappointmentonly, attributes.wheelchairaccessible FROM service INNER JOIN attributes ON service.business_id = attributes.business_id",
			"SELECT service.name, hours.sunday, hours.monday, hours.tuesday, hours.wednesday, hours.thursday, hours.friday, hours.saturday FROM service INNER JOIN hours ON service.business_id = hours.business_id",
			"SELECT service.name, businessparking.garage, businessparking.street, businessparking.validated, businessparking.lot, businessparking.valet FROM service INNER JOIN businessparking ON service.business_id = businessparking.business_id",
			"SELECT user_id, name, review_count, yelping_since, fans FROM customer",
			"SELECT customer.name, review.business_id, review.stars, review.date FROM customer INNER JOIN review ON customer.user_id = review.user_id"
		};

		// Strings representing the beginning result Strings for each option in OPTIONS, respectively:
		static final String[] RESULT_STRINGS = {
			"--Business_Id, Name, Stars, Review Count--\n",
			"--Business Name, Address, City, State, and Postal Code--\n",
			"--Business Name, Bike Parking, Good For Kids, By Appointment Only, Wheelchair Accessible--\n",
			"--Business Name, Sunday Hours, Monday Hours, Tuesday Hours, Wednesday Hours, Thursday Hours, Friday Hours, Saturday Hours--\n",
			"--Business Name, Garage, Street, Validated, Lot, Valet--\n",
			"--User ID, Customer Name, Review Count, Yelping Since, Fans--\n",
			"--Customer Name, Business Reviewed, Stars Given, Review Timestamp--\n"
		};

		// The number of columns for each option in OPTIONS, respectively:
		static final int[] NUM_COLUMNS = {4, 5, 5, 8, 6, 5, 4};
}
