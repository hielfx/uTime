(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('TagDetailController', TagDetailController);

    TagDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Tag', 'Ability'];

    function TagDetailController($scope, $rootScope, $stateParams, entity, Tag, Ability) {
        var vm = this;
        vm.tag = entity;
        vm.load = function (id) {
            Tag.get({id: id}, function(result) {
                vm.tag = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:tagUpdate', function(event, result) {
            vm.tag = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
