'use strict';

angular.module('volunteercrowdApp')
    .factory('AssessmentSearch', function ($resource) {
        return $resource('api/_search/assessments/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
