'use strict';

angular.module('volunteercrowdApp')
    .factory('AdministratorSearch', function ($resource) {
        return $resource('api/_search/administrators/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
