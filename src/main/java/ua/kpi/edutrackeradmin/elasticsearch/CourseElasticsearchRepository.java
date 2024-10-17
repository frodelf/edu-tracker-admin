package ua.kpi.edutrackeradmin.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CourseElasticsearchRepository extends ElasticsearchRepository<CourseIndex, Long> {
    @Query("""
        {"bool": {
            "should": [
                {
                    "multi_match": {
                        "query": "?0",
                        "operator": "or"
                    }
                },
                {"prefix": {
                    "name": {
                        "value": "?0",
                        "boost": 2
                    }
                }},
                {"match": {
                    "name": {
                        "query": "?0",
                        "fuzziness": "AUTO",
                        "boost": 2
                    }
                }},
                {"prefix": {
                    "goal": "?0"
                }},
                {"match": {
                    "goal": {
                        "query": "?0",
                        "fuzziness": "AUTO"
                    }
                }}
            ]
        }}
    """)
    Page<CourseIndex> searchByPrefixOrMatch(String query, Pageable pageable);
}