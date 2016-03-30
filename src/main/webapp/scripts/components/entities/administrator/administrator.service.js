'use strict';

angular.module('volunteercrowdApp')
    .factory('Administrator', function ($resource, DateUtils) {
        return $resource('api/administrators/:id', {}, {
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
