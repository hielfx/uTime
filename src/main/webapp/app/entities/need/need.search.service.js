(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .factory('NeedSearch', NeedSearch);

    NeedSearch.$inject = ['$resource'];

    function NeedSearch($resource) {
        var resourceUrl =  'api/_search/needs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
