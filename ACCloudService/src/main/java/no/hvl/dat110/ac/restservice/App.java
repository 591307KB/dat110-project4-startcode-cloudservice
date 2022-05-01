package no.hvl.dat110.ac.restservice;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App {

	static AccessLog accesslog = null;
	static AccessCode accesscode = null;

	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		// objects for data stored in the service

		accesslog = new AccessLog();
		accesscode = new AccessCode();

		after((req, res) -> {
			res.type("application/json");
		});

		// for basic testing purposes
		get("/accessdevice/hello", (req, res) -> {

			Gson gson = new Gson();

			return gson.toJson("IoT Access Control Device");
		});

		// implement the routes required for the access control service
		// as per the HTTP/REST operations describined in the project description

		get("/accessdevice/log/", (req, res) -> {
			return accesslog.toJson();
		});
		
		delete("/accessdevice/log/", (req, res) -> {
			accesslog.clear();
			return accesslog.toJson();
		});

		post("/accessdevice/log/", (req, res) -> {
			AccessMessage am = new Gson().fromJson(req.body(), AccessMessage.class);
			accesslog.add(am.getMessage());
			return new Gson().toJson("OK");
		});

		get("/accessdevice/log/:id", (req, res) -> {
			return new Gson().toJson(accesslog.get(Integer.parseInt(req.params(":id"))));
		});

		put("/accessdevice/code", (req, res) -> {
			accesscode.setAccesscode(new Gson().fromJson(req.body(), AccessCode.class).getAccesscode());
			return new Gson().toJson(accesscode);
		});

		get("/accessdevice/code", (req, res) -> {
			return new Gson().toJson(accesscode);
		});

		
	}

}
