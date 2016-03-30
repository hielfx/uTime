'use strict';

angular.module('volunteercrowdApp')
    .controller('NeededAbilityController', function ($scope, $state, NeededAbility, NeededAbilitySearch, ParseLinks) {

        $scope.neededAbilitys = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            NeededAbility.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.neededAbilitys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            NeededAbilitySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.neededAbilitys = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.neededAbility = {
                name: null,
                id: null
            };
        };
    });
