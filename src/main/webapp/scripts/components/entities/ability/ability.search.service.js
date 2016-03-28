'use strict';

angular.module('volunteercrowdApp')
    .factory('AbilitySearch', function ($resource) {
        return $resource('api/_search/abilitys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
