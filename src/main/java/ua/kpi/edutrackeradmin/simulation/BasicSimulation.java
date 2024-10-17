package ua.kpi.edutrackeradmin.simulation;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class BasicSimulation extends Simulation {
    HttpProtocolBuilder httpProtocolBuilder = HttpDsl.http.baseUrl("http://slj.demodev.cc:7657/edu-tracker/student")
        .acceptHeader("application/json")
          .maxConnectionsPerHost(10)
            .userAgentHeader("GatlingTest");

    ScenarioBuilder scenarioBuilder = scenario("Basic Load Test").exec(
            http("Visit login page").get("/login").check(status().is(200)).check(
                    regex("Edu Tracker Student").exists()
            )
    );
    {
        setUp(scenarioBuilder.injectOpen(rampUsers(2000).during(10))).protocols(httpProtocolBuilder);
    }
}