'use strict';

angular.module('volunteercrowdApp')
    .factory('NeedSearch', function ($resource) {
        return $resource('api/_search/needs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
