(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('app-user', {
            parent: 'entity',
            url: '/app-user?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.appUser.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/app-user/app-users.html',
                    controller: 'AppUserController',
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
                    $translatePartialLoader.addPart('appUser');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('app-user-detail', {
            parent: 'entity',
            url: '/app-user/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.appUser.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/app-user/app-user-detail.html',
                    controller: 'AppUserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('appUser');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AppUser', function($stateParams, AppUser) {
                    return AppUser.get({id : $stateParams.id});
                }]
            }
        })
        .state('app-user.new', {
            parent: 'app-user',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/app-user/app-user-dialog.html',
                    controller: 'AppUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                phoneNumber: null,
                                isOnline: false,
                                tokens: null,
                                image: null,
                                imageContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('app-user', null, { reload: true });
                }, function() {
                    $state.go('app-user');
                });
            }]
        })
        .state('app-user.edit', {
            parent: 'app-user',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/app-user/app-user-dialog.html',
                    controller: 'AppUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AppUser', function(AppUser) {
                            return AppUser.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('app-user', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('app-user.delete', {
            parent: 'app-user',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/app-user/app-user-delete-dialog.html',
                    controller: 'AppUserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AppUser', function(AppUser) {
                            return AppUser.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('app-user', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
