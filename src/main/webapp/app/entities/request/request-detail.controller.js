(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('RequestDetailController', RequestDetailController);

    RequestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Request', 'AppUser', 'Need', 'Payment', 'Incidence', 'RequestStatus'];

    function RequestDetailController($scope, $rootScope, $stateParams, entity, Request, AppUser, Need, Payment, Incidence, RequestStatus) {
        var vm = this;
        vm.request = entity;
        vm.load = function (id) {
            Request.get({id: id}, function(result) {
                vm.request = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:requestUpdate', function(event, result) {
            vm.request = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
