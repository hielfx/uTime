(function () {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .factory('NeededAbilitySearch', NeededAbilitySearch);

    NeededAbilitySearch.$inject = ['$resource'];

    function NeededAbilitySearch($resource) {
        var resourceUrl = 'api/_search/needed-abilities/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true}
        });
    }
})();
