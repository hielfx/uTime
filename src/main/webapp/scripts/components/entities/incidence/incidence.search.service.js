'use strict';

angular.module('volunteercrowdApp')
    .factory('IncidenceSearch', function ($resource) {
        return $resource('api/_search/incidences/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
