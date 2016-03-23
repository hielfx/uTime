'use strict';

angular.module('volunteercrowdApp')
    .factory('RequestSearch', function ($resource) {
        return $resource('api/_search/requests/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
