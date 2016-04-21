(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('needed-ability', {
            parent: 'entity',
            url: '/needed-ability?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.neededAbility.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/needed-ability/needed-abilities.html',
                    controller: 'NeededAbilityController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('neededAbility');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('needed-ability-detail', {
            parent: 'entity',
            url: '/needed-ability/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.neededAbility.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/needed-ability/needed-ability-detail.html',
                    controller: 'NeededAbilityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('neededAbility');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'NeededAbility', function($stateParams, NeededAbility) {
                    return NeededAbility.get({id : $stateParams.id});
                }]
            }
        })
        .state('needed-ability.new', {
            parent: 'needed-ability',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/needed-ability/needed-ability-dialog.html',
                    controller: 'NeededAbilityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('needed-ability', null, { reload: true });
                }, function() {
                    $state.go('needed-ability');
                });
            }]
        })
        .state('needed-ability.edit', {
            parent: 'needed-ability',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/needed-ability/needed-ability-dialog.html',
                    controller: 'NeededAbilityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NeededAbility', function(NeededAbility) {
                            return NeededAbility.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('needed-ability', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('needed-ability.delete', {
            parent: 'needed-ability',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/needed-ability/needed-ability-delete-dialog.html',
                    controller: 'NeededAbilityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NeededAbility', function(NeededAbility) {
                            return NeededAbility.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('needed-ability', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
