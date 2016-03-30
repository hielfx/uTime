'use strict';

angular.module('volunteercrowdApp')
    .factory('Payment', function ($resource, DateUtils) {
        return $resource('api/payments/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.paymentMoment = DateUtils.convertDateTimeFromServer(data.paymentMoment);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
