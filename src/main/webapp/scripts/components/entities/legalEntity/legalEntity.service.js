'use strict';

angular.module('volunteercrowdApp')
    .factory('LegalEntity', function ($resource, DateUtils) {
        return $resource('api/legalEntitys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
