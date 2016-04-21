(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .factory('AppUserSearch', AppUserSearch);

    AppUserSearch.$inject = ['$resource'];

    function AppUserSearch($resource) {
        var resourceUrl =  'api/_search/app-users/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
