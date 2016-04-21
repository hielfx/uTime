(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .factory('NaturalPersonSearch', NaturalPersonSearch);

    NaturalPersonSearch.$inject = ['$resource'];

    function NaturalPersonSearch($resource) {
        var resourceUrl =  'api/_search/natural-people/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
