package pl.edu.agh.tai.web.bing.map.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;

import java.util.List;

/**
 * Created by root on 5/05/16.
 */
public class GeoModuleExt extends SimpleModule {

    public GeoModuleExt() {
        super("Mixins", new Version(1, 0, 0, null));
        setMixInAnnotation(GeoResult.class, GeoResultMixin.class);
        setMixInAnnotation(GeoResults.class, GeoResultsMixin.class);
    }

    static abstract class GeoResultMixin {
        GeoResultMixin(@JsonProperty("content") Object content,
                       @JsonProperty("distance") Distance distance) {
        }
    }

    static abstract class GeoResultsMixin {
        GeoResultsMixin(@JsonProperty("results") List<GeoResult> results,
                        @JsonProperty("averageDistance") Distance averageDistance) {
        }

        @JsonProperty("results")
        abstract List<GeoResult> getContent();
    }
}
