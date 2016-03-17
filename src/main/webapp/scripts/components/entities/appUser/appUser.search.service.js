'use strict';

angular.module('volunteercrowdApp')
    .factory('AppUserSearch', function ($resource) {
        return $resource('api/_search/appUsers/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
