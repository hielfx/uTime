(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .factory('AbilitySearch', AbilitySearch);

    AbilitySearch.$inject = ['$resource'];

    function AbilitySearch($resource) {
        var resourceUrl =  'api/_search/abilities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
