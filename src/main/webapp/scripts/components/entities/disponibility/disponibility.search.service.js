'use strict';

angular.module('volunteercrowdApp')
    .factory('DisponibilitySearch', function ($resource) {
        return $resource('api/_search/disponibilitys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
