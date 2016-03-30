'use strict';

angular.module('volunteercrowdApp')
    .factory('NeededAbilitySearch', function ($resource) {
        return $resource('api/_search/neededAbilitys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
