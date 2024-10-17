package ua.kpi.edutrackeradmin.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

@Setting(settingPath = "/elasticsearch-settings.json")
@Document(indexName = "courses_index")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseIndex {
    private Long id;
    private String name;
    private String goal;
}