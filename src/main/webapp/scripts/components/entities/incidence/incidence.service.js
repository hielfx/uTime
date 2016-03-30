'use strict';

angular.module('volunteercrowdApp')
    .factory('Incidence', function ($resource, DateUtils) {
        return $resource('api/incidences/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creationDate = DateUtils.convertDateTimeFromServer(data.creationDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
