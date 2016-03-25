'use strict';

angular.module('volunteercrowdApp')
    .factory('CurriculumSearch', function ($resource) {
        return $resource('api/_search/curriculums/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
