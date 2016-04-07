'use strict';

angular.module('volunteercrowdApp')
    .factory('AvailabilitySearch', function ($resource) {
        return $resource('api/_search/availabilitys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
