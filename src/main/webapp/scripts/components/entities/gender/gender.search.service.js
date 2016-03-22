'use strict';

angular.module('volunteercrowdApp')
    .factory('GenderSearch', function ($resource) {
        return $resource('api/_search/genders/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
