'use strict';

angular.module('volunteercrowdApp')
    .factory('Ability', function ($resource, DateUtils) {
        return $resource('api/abilitys/:id', {}, {
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
