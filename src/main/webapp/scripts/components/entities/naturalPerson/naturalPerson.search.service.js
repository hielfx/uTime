'use strict';

angular.module('volunteercrowdApp')
    .factory('NaturalPersonSearch', function ($resource) {
        return $resource('api/_search/naturalPersons/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
