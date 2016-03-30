'use strict';

angular.module('volunteercrowdApp')
    .factory('NeededAbility', function ($resource, DateUtils) {
        return $resource('api/neededAbilitys/:id', {}, {
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
